package hu.sed.evaluator.task.evaluators;

import hu.sed.evaluator.annotation.syntax.TypeCheck;
import hu.sed.evaluator.item.ItemFactory;
import hu.sed.evaluator.item.syntax.TypeItem;
import hu.sed.evaluator.task.evaluators.syntax.SyntaxElement;
import hu.sed.evaluator.task.evaluators.syntax.EvaluatorService;
import hu.sed.evaluator.task.evaluators.syntax.TypeItemEvaluator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Map;

import static hu.sed.evaluator.task.evaluators.syntax.SyntaxElement.*;
import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

public class TypeItemEvaluatorTest {
    ItemFactory itemFactory;

    @Mock
    EvaluatorService evaluatorService;

    TypeItemEvaluator typeItemEvaluator;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        itemFactory = new ItemFactory();
        typeItemEvaluator = new TypeItemEvaluator(evaluatorService, itemFactory);
    }

    @Test
    public void simpleTypeItemCheck() {
        // GIVEN
        Class<TestClass> testClass = TestClass.class;
        TypeItem item = itemFactory.createItem(testClass.getAnnotation(TypeCheck.class), testClass);
        when(evaluatorService.checkType(any(), any())).thenReturn(true);
        when(evaluatorService.checkTypesInAnyOrder(any(), any())).thenReturn(true);
        when(evaluatorService.checkModifiers(anyInt(), anyInt())).thenReturn(true);

        // WHEN
        ScoredItem scoredItem = typeItemEvaluator.evaluate(item);

        // THEN
        assertThat(scoredItem.getScore()).isEqualTo(1.0);
        assertThat(scoredItem.getItem()).isEqualTo(item);

        Map<SyntaxElement, Boolean> checkedElements = scoredItem.getCheckedElements();
        assertThat(checkedElements.keySet()).containsExactlyInAnyOrder(EXISTENCE, MODIFIERS, PARENT_CLASS, INTERFACES);
        assertTrue(checkedElements.get(EXISTENCE));
        assertTrue(checkedElements.get(MODIFIERS));
        assertTrue(checkedElements.get(PARENT_CLASS));
        assertTrue(checkedElements.get(INTERFACES));
    }

    @Test
    public void simpleTypeDoesNotExists() {
        // GIVEN
        Class<TestClass> testClass = TestClass.class;
        TypeItem item = itemFactory.createItem(testClass.getAnnotation(TypeCheck.class), testClass);
        item.setName("nonExistingClass");

        // WHEN
        ScoredItem scoredItem = typeItemEvaluator.evaluate(item);

        // THEN
        assertThat(scoredItem.getScore()).isEqualTo(0.0);
        assertThat(scoredItem.getItem()).isEqualTo(item);

        Map<SyntaxElement, Boolean> checkedElements = scoredItem.getCheckedElements();
        assertThat(checkedElements.keySet()).containsExactlyInAnyOrder(EXISTENCE);
        assertFalse(checkedElements.get(EXISTENCE));
    }

    @Test
    public void simpleTypeShouldBeInterface() {
        // GIVEN
        Class<TestClass> testClass = TestClass.class;
        TypeItem item = itemFactory.createItem(testClass.getAnnotation(TypeCheck.class), testClass);
        item.setInterfce(true);

        // WHEN
        ScoredItem scoredItem = typeItemEvaluator.evaluate(item);

        // THEN
        assertThat(scoredItem.getScore()).isEqualTo(0.0);
        assertThat(scoredItem.getItem()).isEqualTo(item);

        Map<SyntaxElement, Boolean> checkedElements = scoredItem.getCheckedElements();
        assertThat(checkedElements.keySet()).containsExactlyInAnyOrder(EXISTENCE);
        assertFalse(checkedElements.get(EXISTENCE));
    }

    @Test
    public void simpleTypeItemCheck2() {
        // GIVEN
        Class<TestClass> testClass = TestClass.class;
        TypeItem item = itemFactory.createItem(testClass.getAnnotation(TypeCheck.class), testClass);
        when(evaluatorService.checkType(any(), any())).thenReturn(false);
        when(evaluatorService.checkTypesInAnyOrder(any(), any())).thenReturn(false);
        when(evaluatorService.checkModifiers(anyInt(), anyInt())).thenReturn(false);

        // WHEN
        ScoredItem scoredItem = typeItemEvaluator.evaluate(item);

        // THEN
        assertThat(scoredItem.getScore()).isEqualTo(0.25);
        assertThat(scoredItem.getItem()).isEqualTo(item);

        Map<SyntaxElement, Boolean> checkedElements = scoredItem.getCheckedElements();
        assertThat(checkedElements.keySet()).containsExactlyInAnyOrder(EXISTENCE, MODIFIERS, PARENT_CLASS, INTERFACES);
        assertTrue(checkedElements.get(EXISTENCE));
        assertFalse(checkedElements.get(MODIFIERS));
        assertFalse(checkedElements.get(PARENT_CLASS));
        assertFalse(checkedElements.get(INTERFACES));
    }

    @TypeCheck
    public static abstract class TestClass implements Comparable<TestClass>, List<TestClass> {
    }
}
