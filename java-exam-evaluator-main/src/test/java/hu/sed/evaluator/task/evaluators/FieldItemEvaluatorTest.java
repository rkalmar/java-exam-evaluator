package hu.sed.evaluator.task.evaluators;

import hu.sed.evaluator.annotation.syntax.FieldCheck;
import hu.sed.evaluator.item.ItemFactory;
import hu.sed.evaluator.item.syntax.FieldItem;
import hu.sed.evaluator.task.evaluators.syntax.EvaluatorService;
import hu.sed.evaluator.task.evaluators.syntax.FieldItemEvaluator;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.lang.reflect.Field;
import java.util.Map;

import static hu.sed.evaluator.task.evaluators.CheckedElement.*;
import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

public class FieldItemEvaluatorTest {

    ItemFactory itemFactory;

    @Mock
    EvaluatorService evaluatorService;

    FieldItemEvaluator fieldItemEvaluator;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        itemFactory = new ItemFactory();
        fieldItemEvaluator = new FieldItemEvaluator(evaluatorService, itemFactory);
    }

    @Test
    public void correctFieldItemTest() {
        // GIVEN
        FieldItem fieldItem = createFieldItem("i");
        when(evaluatorService.checkType(any(), any())).thenReturn(true);
        when(evaluatorService.checkModifiers(anyInt(), anyInt())).thenReturn(true);

        // WHEN
        ScoredItem scoredItem = fieldItemEvaluator.evaluate(fieldItem);

        // THEN
        assertThat(scoredItem.getScore()).isEqualTo(1.0);

        Map<CheckedElement, Boolean> checkedElements = scoredItem.getCheckedElements();
        assertThat(checkedElements.keySet()).containsExactlyInAnyOrder(EXISTANCE, MODIFIERS);
        assertTrue(checkedElements.get(EXISTANCE));
        assertTrue(checkedElements.get(MODIFIERS));
    }

    @Test
    public void checkFieldDoesNotExist() {
        // GIVEN
        FieldItem fieldItem = createFieldItem("i");
        when(evaluatorService.checkType(any(), any())).thenReturn(false);
        when(evaluatorService.checkModifiers(anyInt(), anyInt())).thenReturn(true);

        // WHEN
        ScoredItem scoredItem = fieldItemEvaluator.evaluate(fieldItem);

        // THEN
        assertThat(scoredItem.getScore()).isEqualTo(0.0);
        assertThat(scoredItem.getItem()).isEqualTo(fieldItem);

        Map<CheckedElement, Boolean> checkedElements = scoredItem.getCheckedElements();
        assertThat(checkedElements.keySet()).containsExactlyInAnyOrder(EXISTANCE);
        assertFalse(checkedElements.get(EXISTANCE));
    }

    @Test
    public void checkFieldWithIncorrectModifiers() {
        // GIVEN
        FieldItem fieldItem = createFieldItem("i");
        when(evaluatorService.checkType(any(), any())).thenReturn(true);
        when(evaluatorService.checkModifiers(anyInt(), anyInt())).thenReturn(false);
        fieldItem.setScore(10);

        // WHEN
        ScoredItem scoredItem = fieldItemEvaluator.evaluate(fieldItem);

        // THEN
        assertThat(scoredItem.getScore()).isEqualTo(5);
        assertThat(scoredItem.getItem()).isEqualTo(fieldItem);

        Map<CheckedElement, Boolean> checkedElements = scoredItem.getCheckedElements();
        assertThat(checkedElements.keySet()).containsExactlyInAnyOrder(EXISTANCE, MODIFIERS);
        assertTrue(checkedElements.get(EXISTANCE));
        assertFalse(checkedElements.get(MODIFIERS));
    }

    @SneakyThrows
    private FieldItem createFieldItem(String fieldName) {
        Field field = FieldItemEvaluatorTest.TestClass.class.getDeclaredField(fieldName);
        return itemFactory.createItem(field.getAnnotation(FieldCheck.class), field);
    }

    public static class TestClass {
        @FieldCheck
        private static int i;
    }
}
