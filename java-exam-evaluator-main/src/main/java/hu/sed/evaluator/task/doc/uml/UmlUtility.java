package hu.sed.evaluator.task.doc.uml;

import hu.sed.evaluator.task.ReflectionUtils;
import lombok.experimental.UtilityClass;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@UtilityClass
public class UmlUtility {

    public UmlRepresentation createUmlRepresentation(String rootPackage) {
        List<Class<?>> classes = ReflectionUtils.getClassesOfPackage(rootPackage);

        final Map<String, List<Class<?>>> examClassesPerPackage = classes.stream()
                .filter(ReflectionUtils::notUmlSkipped)
                .collect(Collectors.groupingBy(Class::getPackageName));

        List<PackageRepresentation> packageRepresentations = new ArrayList<>();
        List<ClassRelationRepresentation> classRelationRepresentations = new ArrayList<>();
        examClassesPerPackage.forEach((packageName, classList) ->
                packageRepresentations.add(
                        PackageRepresentation.of(packageName,
                                classList.stream()
                                        .map(UmlUtility::toClassRepresentation)
                                        .collect(Collectors.toList())
                        )
                ));


        for (int i = 0; i < classes.size(); i++) {
            for (int j = i + 1; j < classes.size(); j++) {
                boolean reverseRelation = false;
                Class<?> aClass = classes.get(i);
                Class<?> bClass = classes.get(j);
                Optional<ClassRelation> relation = getRelation(aClass, bClass);
                if (relation.isEmpty()) {
                    relation = getRelation(bClass, aClass);
                    reverseRelation = true;
                }
                if (relation.isPresent()) {
                    classRelationRepresentations.add(ClassRelationRepresentation.of(aClass, bClass, relation.get(), reverseRelation));
                }
            }
        }

        return RootUmlRepresentation.builder()
                .packageRepresentations(packageRepresentations)
                .classRelationRepresentations(classRelationRepresentations)
                .build();
    }

    private static ClassRepresentation toClassRepresentation(Class<?> clazz) {
        return ClassRepresentation.of(clazz,
                Arrays.stream(clazz.getDeclaredFields())
                        .filter(ReflectionUtils::notUmlSkipped)
                        .sorted(Comparator.comparing(Field::getName))
                        .map(FieldRepresentation::of)
                        .collect(Collectors.toList()),
                Arrays.stream(clazz.getDeclaredMethods())
                        .filter(ReflectionUtils::notUmlSkipped)
                        .sorted(Comparator.comparing(Method::getName))
                        .map(MethodRepresentation::of)
                        .collect(Collectors.toList())
        );
    }

    private Optional<ClassRelation> getRelation(Class<?> aClass, Class<?> bClass) {
        if (hasAggregation(aClass, bClass)) {
            if (hasComposition(aClass, bClass)) {
                return Optional.of(ClassRelation.Composition);
            }
            return Optional.of(ClassRelation.Aggregation);
        } else if (hasSpecialization(aClass, bClass)) {
            return Optional.of(ClassRelation.Specialization);
        } else if (hasAssociation(aClass, bClass)) {
            return Optional.of(ClassRelation.Association);
        }
        return Optional.empty();
    }

    private boolean hasAssociation(Class<?> aClass, Class<?> bClass) {
        return Arrays.stream(aClass.getDeclaredMethods())
                .anyMatch(method -> hasAssociation(method, bClass));
    }

    private boolean hasAssociation(Method method, Class<?> clazz) {
        List<Type> types = new ArrayList<>(Arrays.stream(method.getGenericParameterTypes()).toList());
        types.add(method.getGenericReturnType());

        return types.stream().anyMatch(
                type -> {
                    if (type.equals(clazz)) {
                        return true;
                    }
                    if (type instanceof ParameterizedType) {
                        for (Type actualTypeArgument : ((ParameterizedType) type).getActualTypeArguments()) {
                            if (actualTypeArgument.equals(clazz)) {
                                return true;
                            }
                        }
                    }
                    return false;
                }
        );
    }

    private boolean hasSpecialization(Class<?> aClass, Class<?> bClass) {
        return bClass.getSuperclass() != null && bClass.getSuperclass().equals(aClass);
    }

    /*
     * Composition only if aClass cannot be constructed without bClass..
     */
    private boolean hasComposition(Class<?> aClass, Class<?> bClass) {
        return Arrays.stream(aClass.getConstructors())
                .allMatch(constructor -> {
                    for (Type genericParameterType : constructor.getGenericParameterTypes()) {
                        if (genericParameterType.equals(bClass)) {
                            return true;
                        }
                    }
                    return false;
                });
    }

    /*
     * Aggregation if aClass is a container of bClass..
     */
    private boolean hasAggregation(Class<?> aClass, Class<?> bClass) {
        for (Field declaredField : aClass.getDeclaredFields()) {
            if (declaredField.getType().equals(bClass)) {
                return true;
            }
            if (declaredField.getGenericType() instanceof ParameterizedType &&
                    (Collection.class.isAssignableFrom(declaredField.getType())
                            || Map.class.isAssignableFrom(declaredField.getType()))) {
                for (Type genericType : ((ParameterizedType) declaredField.getGenericType()).getActualTypeArguments()) {
                    if (genericType.equals(bClass)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
