package hu.sed.evaluator.task.doc.uml;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
class RootUmlRepresentation implements UmlRepresentation {

    List<PackageRepresentation> packageRepresentations;
    List<ClassRelationRepresentation> classRelationRepresentations;

    @Override
    public String represent() {
        StringBuilder representation = new StringBuilder();
        representation.append("@startuml")
                .append(System.lineSeparator())
                .append("skinparam classAttributeIconSize 0")
                .append(System.lineSeparator());

        packageRepresentations.forEach(packageRepresentation -> representation.append(packageRepresentation.represent()));
        classRelationRepresentations.forEach(classRelationRepresentations -> representation.append(classRelationRepresentations.represent()));

        representation.append("@enduml")
                .append(System.lineSeparator());

        return representation.toString();
    }
}
