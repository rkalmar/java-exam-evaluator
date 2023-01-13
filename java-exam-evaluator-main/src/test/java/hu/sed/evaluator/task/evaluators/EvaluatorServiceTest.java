package hu.sed.evaluator.task.evaluators;

import hu.sed.evaluator.item.element.TypeDefinition;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class EvaluatorServiceTest {

    EvaluatorService evaluatorService = new EvaluatorService();

    @ParameterizedTest
    @ValueSource(ints = {0, 1, -1})
    public void checkModifierTest(int differenceBetweenModifiers) {
        // GIVEN
        int expectedModifier = 100;
        int actualModifier = expectedModifier + differenceBetweenModifiers;

        // WHEN
        boolean modifiersMatch = evaluatorService.checkModifiers(actualModifier, expectedModifier);

        // THEN
        if (differenceBetweenModifiers == 0) {
            assertTrue(modifiersMatch);
        } else {
            assertFalse(modifiersMatch);
        }
    }

    @Test
    public void checkSimpleTypeTest() {
        // GIVEN
        TypeDefinition typeDefinition = TypeDefinition.builder().type("int").build();

        // WHEN
        assertTrue(evaluatorService.checkType(typeDefinition, typeDefinition));
    }

    @Test
    public void checkGenericTypeTest() {
        // GIVEN
        TypeDefinition[] genericTypes = new TypeDefinition[2];
        genericTypes[0] = TypeDefinition.builder().type("java.lang.Integer").build();
        genericTypes[1] = TypeDefinition.builder().type("java.lang.String").build();
        TypeDefinition typeDefinition = TypeDefinition.builder()
                .type("java.util.Map")
                .genericTypes(genericTypes)
                .build();

        // WHEN
        assertTrue(evaluatorService.checkType(typeDefinition, typeDefinition));
    }

    @Test
    public void checkSimpleTypeDoesNotMatchTest() {
        // GIVEN
        TypeDefinition actualTypeDef = TypeDefinition.builder().type("int").build();
        TypeDefinition expectedTypeDef = TypeDefinition.builder().type("java.lang.Integer").build();

        // WHEN
        assertFalse(evaluatorService.checkType(actualTypeDef, expectedTypeDef));
    }

    @Test
    public void checkGenericTypeListDoesNotMatchTest() {
        // GIVEN
        TypeDefinition actualTypeDef = TypeDefinition.builder()
                .type("java.util.List")
                .genericTypes(new TypeDefinition[]{
                        TypeDefinition.builder().type("java.lang.Integer").build()
                })
                .build();

        TypeDefinition expectedTypeDef = TypeDefinition.builder()
                .type("java.util.List")
                .build();

        // WHEN
        assertFalse(evaluatorService.checkType(actualTypeDef, expectedTypeDef));
    }


    @Test
    public void checkGenericTypeDoesNotMatchTest() {
        // GIVEN
        TypeDefinition[] actualGenericTypes = new TypeDefinition[]{
                TypeDefinition.builder().type("java.lang.Integer").build(),
                TypeDefinition.builder().type("java.lang.String").build()
        };

        TypeDefinition[] expectedGenericTypes = new TypeDefinition[]{actualGenericTypes[1], actualGenericTypes[0]};

        TypeDefinition actualTypeDef = TypeDefinition.builder()
                .type("java.util.Map")
                .genericTypes(actualGenericTypes)
                .build();

        TypeDefinition expectedTypeDef = TypeDefinition.builder()
                .type("java.util.Map")
                .genericTypes(expectedGenericTypes)
                .build();

        // WHEN
        assertFalse(evaluatorService.checkType(actualTypeDef, expectedTypeDef));
    }

}
