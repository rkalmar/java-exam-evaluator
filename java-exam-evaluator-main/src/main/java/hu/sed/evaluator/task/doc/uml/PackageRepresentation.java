package hu.sed.evaluator.task.doc.uml;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.List;

@RequiredArgsConstructor(staticName = "of")
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
class PackageRepresentation implements UmlRepresentation {

    String packageName;

    List<ClassRepresentation> classList;

    @Override
    public String represent() {
        StringBuilder representation = new StringBuilder();
        representation.append("package ")
                .append(packageName)
                .append(" <<Frame>> {")
                .append(System.lineSeparator());

        classList.forEach(clazz -> representation.append(clazz.represent()));

        representation.append("}")
                .append(System.lineSeparator());
        return representation.toString();
    }
}
