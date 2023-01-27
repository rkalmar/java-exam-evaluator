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
        } else {
            clazzRepresentation.append("class");
        }
        clazzRepresentation.append(" ").append(clazz.getSimpleName()).append(" {");

        clazzRepresentation.append(System.lineSeparator());

        fieldRepresentations.forEach(fieldRepresentation -> clazzRepresentation.append(fieldRepresentation.represent()));
        methodRepresentations.forEach(methodRepresentation -> clazzRepresentation.append(methodRepresentation.represent()));

        clazzRepresentation.append("}")
                .append(System.lineSeparator());
        return clazzRepresentation.toString();
    }
}
