package hu.sed.evaluator.task.doc.uml;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.lang.reflect.Field;

@RequiredArgsConstructor(staticName = "of")
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class FieldRepresentation implements UmlRepresentation {

    Field field;

    @Override
    public String getName() {
        return field.getName();
    }

    @Override
    public String represent() {
        return "-" + field.getName() + System.lineSeparator();
    }
}
