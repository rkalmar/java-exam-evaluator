package hu.sed.evaluator.task.doc.uml;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.lang.reflect.Modifier;
import java.util.List;

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

        if (clazz.isEnum()) {
            fieldRepresentations.stream()
                    .map(FieldRepresentation::represent)
                    .filter(representation -> !representation.contains("$"))
                    .forEach(clazzRepresentation::append);
            methodRepresentations.stream()
                    .map(MethodRepresentation::represent)
                    .filter(representation -> !representation.contains("$"))
                    .filter(representation -> !representation.contains("valueOf") && !representation.contains("values"))
                    .forEach(clazzRepresentation::append);
        } else {
            fieldRepresentations.stream()
                    .map(FieldRepresentation::represent)
                    .forEach(clazzRepresentation::append);
            methodRepresentations.stream()
                    .map(MethodRepresentation::represent)
                    .forEach(clazzRepresentation::append);
        }

        clazzRepresentation.append("}")
                .append(System.lineSeparator());
        return clazzRepresentation.toString();
    }
}
