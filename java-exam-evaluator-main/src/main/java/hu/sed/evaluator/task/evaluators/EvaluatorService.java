package hu.sed.evaluator.task.evaluators;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import hu.sed.evaluator.item.element.TypeDefinition;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Singleton
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class EvaluatorService {

    public boolean checkModifiers(int actualModifiers, int expectedModifiers) {
        return actualModifiers == expectedModifiers;
    }

    public boolean checkType(TypeDefinition actualType, TypeDefinition expectedType) {
        if (actualType.getType().equals(expectedType.getType())
                || actualType.getGenericTypes().length == expectedType.getGenericTypes().length) {
            return false;
        }

        for (int i = 0; i < actualType.getGenericTypes().length; i++) {
            TypeDefinition actualGenericType = actualType.getGenericTypes()[i];
            TypeDefinition expectedGenericType = expectedType.getGenericTypes()[i];
            if (!checkType(actualGenericType, expectedGenericType)) {
                return false;
            }
        }

        return true;
    }

}
