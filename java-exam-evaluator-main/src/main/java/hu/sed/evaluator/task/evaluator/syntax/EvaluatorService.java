package hu.sed.evaluator.task.evaluator.syntax;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import hu.sed.evaluator.item.element.TypeDefinition;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.lang.reflect.Method;
import java.util.Arrays;

@Singleton
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class EvaluatorService {

    public boolean checkModifiers(int actualModifiers, int expectedModifiers) {
        return actualModifiers == expectedModifiers;
    }

    public boolean checkType(TypeDefinition actualType, TypeDefinition expectedType) {
        if (!actualType.getType().equals(expectedType.getType())) {
            return false;
        }

        return checkTypesInOrder(actualType.getGenericTypes(), expectedType.getGenericTypes());
    }

    public boolean checkTypesInOrder(TypeDefinition[] actualTypes, TypeDefinition[] expectedTypes) {
        return checkTypes(actualTypes, expectedTypes, true);
    }

    public boolean checkTypesInAnyOrder(TypeDefinition[] actualTypes, TypeDefinition[] expectedTypes) {
        return checkTypes(actualTypes, expectedTypes, false);
    }

    private boolean checkTypes(TypeDefinition[] actualTypes, TypeDefinition[] expectedTypes, boolean checkInOrder) {
        if (actualTypes.length != expectedTypes.length) {
            return false;
        }

        for (int i = 0; i < actualTypes.length; i++) {
            TypeDefinition actualType = actualTypes[i];

            if (checkInOrder) {
                TypeDefinition expectedType = expectedTypes[i];
                if (!checkType(actualType, expectedType)) {
                    return false;
                }
            } else {
                if (Arrays.stream(expectedTypes)
                        .noneMatch(expType -> checkType(actualType, expType))) {
                    return false;
                }
            }
        }
        return true;
    }
}
