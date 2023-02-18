package hu.sed.evaluator.task.doc.uml;

import hu.sed.evaluator.annotation.uml.UmlFilter;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Modifier;
import java.util.List;
import java.util.function.Predicate;

@RequiredArgsConstructor(staticName = "of")
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
class ClassRepresentation implements UmlRepresentation {

    Class<?> clazz;
    List<FieldRepresentation> fieldRepresentations;
    List<MethodRepresentation> methodRepresentations;

    @Override
    public String represent() {
        StringBuilder clazzRepresentation = new StringBuilder();
        if (clazz.isInterface()) {
            clazzRepresentation.append("interface");
        } else if (Modifier.isAbstract(clazz.getModifiers())) {
            clazzRepresentation.append("abstract class");
        } else if (Throwable.class.isAssignableFrom(clazz)) {
            clazzRepresentation.append("exception");
        } else if (clazz.isEnum()) {
            clazzRepresentation.append("enum");
        } else {
            clazzRepresentation.append("class");
        }
        clazzRepresentation.append(" ").append(clazz.getSimpleName()).append(" {");

        clazzRepresentation.append(System.lineSeparator());

        Predicate<MethodRepresentation> methodPredicate = (methodRepresentation) -> true;
        if (clazz.isAnnotationPresent(UmlFilter.class)) {
            UmlFilter umlFilter = clazz.getAnnotation(UmlFilter.class);
            for (String filter : umlFilter.methodPrefixes()) {
                if (StringUtils.isNotBlank(filter)) {
                    methodPredicate = methodPredicate.and(methodRepresentation ->
                        !methodRepresentation.getName().startsWith(filter)
                    );
                }
            }
        }

        if (clazz.isEnum()) {
            fieldRepresentations.stream()
                    .filter(representation -> !representation.getName().contains("$"))
                    .map(FieldRepresentation::represent)
                    .forEach(clazzRepresentation::append);
            methodRepresentations.stream()
                    .filter(representation -> !representation.getName().contains("$"))
                    .filter(representation -> !representation.getName().contains("valueOf") &&
                            !representation.getName().contains("values"))
                    .filter(methodPredicate)
                    .map(MethodRepresentation::represent)
                    .forEach(clazzRepresentation::append);
        } else {
            fieldRepresentations.stream()
                    .map(FieldRepresentation::represent)
                    .forEach(clazzRepresentation::append);
            methodRepresentations.stream()
                    .filter(methodPredicate)
                    .map(MethodRepresentation::represent)
                    .forEach(clazzRepresentation::append);
        }

        clazzRepresentation.append("}")
                .append(System.lineSeparator());
        return clazzRepresentation.toString();
    }
}
