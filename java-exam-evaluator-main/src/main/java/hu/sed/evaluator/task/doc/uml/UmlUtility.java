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
        List<? extends Class<?>> classes = ReflectionUtils.getClassesOfPackage(rootPackage).stream()
                .filter(ReflectionUtils::notUmlSkipped).toList();

        final Map<String, List<Class<?>>> examClassesPerPackage = classes.stream()
                .collect(Collectors.groupingBy(Class::getPackageName));

        List<PackageRepresentation> packageRepresentations = new ArrayList<>();
        List<ClassRelationRepresentation> classRelationRepresentations = new ArrayList<>();
        examClassesPerPackage.forEach((packageName, classList) ->
                packageRepresentations.add(
                        PackageRepresentation.of(packageName,
                                classList.stream()
                                        .map(UmlUtility::toClassRepresentation)
                                        .toList()
                        )
                ));


        for (int i = 0; i < classes.size(); i++) {
            for (int j = i + 1; j < classes.size(); j++) {
                boolean reverseRelation = false;
                Class<?> classA = classes.get(i);
                Class<?> classB = classes.get(j);
                Optional<ClassRelation> relation = getRelation(classA, classB);
                if (relation.isEmpty()) {
                    relation = getRelation(classB, classA);
                    reverseRelation = true;
                }
                if (relation.isPresent()) {
                    classRelationRepresentations.add(ClassRelationRepresentation.of(classA, classB, relation.get(), reverseRelation));
                }
            }
        }

        return RootUmlRepresentation.builder()
                .packageRepresentations(packageRepresentations)
                .classRelationRepresentations(classRelationRepresentations)
                .build();
    }

    private ClassRepresentation toClassRepresentation(Class<?> clazz) {
        return ClassRepresentation.of(clazz,
                Arrays.stream(clazz.getDeclaredFields())
                        .filter(ReflectionUtils::notUmlSkipped)
                        .sorted(Comparator.comparing(Field::getName))
                        .map(FieldRepresentation::of)
                        .toList(),
                Arrays.stream(clazz.getDeclaredMethods())
                        .filter(ReflectionUtils::notUmlSkipped)
                        .sorted(Comparator.comparing(Method::getName))
                        .map(MethodRepresentation::of)
                        .toList()
        );
    }

    private Optional<ClassRelation> getRelation(Class<?> classA, Class<?> classB) {
        if (hasAggregation(classA, classB)) {
            if (hasComposition(classA, classB)) {
                return Optional.of(ClassRelation.Composition);
            }
            return Optional.of(ClassRelation.Aggregation);
        } else if (isSpecialization(classA, classB)) {
            return Optional.of(ClassRelation.Specialization);
        } else if (isImplementation(classA, classB)) {
            return Optional.of(ClassRelation.Implementation);
        } else if (hasAssociation(classA, classB)) {
            return Optional.of(ClassRelation.Association);
        } else if (Throwable.class.isAssignableFrom(classB) && doesThrow(classA, classB)) {
            return Optional.of(ClassRelation.ThrowsException);
        }
        return Optional.empty();
    }

    private boolean isImplementation(Class<?> classA, Class<?> classB) {
        return matchAnyType(Arrays.asList(classB.getGenericInterfaces()), classA);
    }

    private boolean hasAssociation(Class<?> classA, Class<?> classB) {
        return Arrays.stream(classA.getDeclaredMethods())
                .anyMatch(method -> hasAssociation(method, classB));
    }

    private boolean hasAssociation(Method method, Class<?> clazz) {
        List<Type> types = new ArrayList<>(Arrays.stream(method.getGenericParameterTypes()).toList());
        types.add(method.getGenericReturnType());

        return matchAnyType(types, clazz);
    }

    private boolean doesThrow(Class<?> classA, Class<?> classB) {
        return Arrays.stream(classA.getDeclaredMethods())
                .anyMatch(method -> doesThrow(method, classB));
    }

    private boolean doesThrow(Method method, Class<?> clazz) {
        return matchAnyType(Arrays.asList(method.getGenericExceptionTypes()), clazz);
    }

    private boolean matchAnyType(List<Type> types, Class<?> clazz) {
        return types.stream().anyMatch(
                type -> {
                    if (type.equals(clazz)) {
                        return true;
                    }
                    if (type instanceof ParameterizedType parameterizedType) {
                        for (Type actualTypeArgument : parameterizedType.getActualTypeArguments()) {
                            if (actualTypeArgument.equals(clazz)) {
                                return true;
                            }
                        }
                    }
                    return false;
                }
        );
    }

    private boolean isSpecialization(Class<?> classA, Class<?> classB) {
        return classB.getSuperclass() != null && classB.getSuperclass().equals(classA);
    }

    /*
     * Composition only if classA cannot be constructed without classB..
     */
    private boolean hasComposition(Class<?> classA, Class<?> classB) {
        return Arrays.stream(classA.getConstructors())
                .allMatch(constructor -> {
                    for (Type genericParameterType : constructor.getGenericParameterTypes()) {
                        if (genericParameterType.equals(classB)) {
                            return true;
                        }
                    }
                    return false;
                });
    }

    /*
     * Aggregation if classA is a container of classB..
     */
    private boolean hasAggregation(Class<?> classA, Class<?> classB) {
        for (Field declaredField : classA.getDeclaredFields()) {
            if (declaredField.getType().equals(classB)) {
                return true;
            }
            if (declaredField.getGenericType() instanceof ParameterizedType parameterizedType &&
                    (Collection.class.isAssignableFrom(declaredField.getType()) ||
                            Map.class.isAssignableFrom(declaredField.getType()))) {
                for (Type genericType : parameterizedType.getActualTypeArguments()) {
                    if (genericType.equals(classB)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
