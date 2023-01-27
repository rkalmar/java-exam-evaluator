package hu.sed.evaluator.task.doc.uml;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RequiredArgsConstructor(staticName = "of")
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
class ClassRelationRepresentation implements UmlRepresentation {

    Class<?> classA;
    Class<?> classB;
    ClassRelation relation;
    boolean reverseRelation;

    @Override
    public String represent() {
        if (reverseRelation) {
            return classB.getSimpleName() + " " + relation.sign + " " + classA.getSimpleName() + "\n";
        } else {
            return classA.getSimpleName() + " " + relation.sign + " " + classB.getSimpleName() + "\n";
        }
    }
}
