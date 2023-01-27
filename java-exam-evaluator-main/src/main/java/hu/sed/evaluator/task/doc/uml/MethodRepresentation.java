package hu.sed.evaluator.task.doc.uml;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.lang.reflect.Method;

@RequiredArgsConstructor(staticName = "of")
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class MethodRepresentation  implements UmlRepresentation {

    Method method;

    @Override
    public String represent() {
        return method.getName() + "()" + System.lineSeparator();
    }
}
