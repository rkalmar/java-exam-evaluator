package hu.sed.evaluator.task.evaluator;

import hu.sed.evaluator.annotation.syntax.ConstructorCheck;
import hu.sed.evaluator.item.ItemFactory;
import hu.sed.evaluator.item.syntax.ConstructorItem;
import hu.sed.evaluator.task.ScoredItem;
import hu.sed.evaluator.task.evaluator.syntax.SyntaxElement;
import hu.sed.evaluator.task.evaluator.syntax.ConstructorItemEvaluator;
import hu.sed.evaluator.task.evaluator.syntax.EvaluatorService;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.lang.reflect.Constructor;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Map;

import static hu.sed.evaluator.task.evaluator.syntax.SyntaxElement.EXCEPTIONS;
import static hu.sed.evaluator.task.evaluator.syntax.SyntaxElement.EXISTENCE;
import static hu.sed.evaluator.task.evaluator.syntax.SyntaxElement.MODIFIERS;
import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

public class ConstructorItemEvaluatorTest {

    ItemFactory itemFactory;

    @Mock
    EvaluatorService evaluatorService;

    ConstructorItemEvaluator constructorItemEvaluator;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        itemFactory = new ItemFactory();
        constructorItemEvaluator = new ConstructorItemEvaluator(evaluatorService, itemFactory);
    }

    @Test
    public void checkDefaultConstructor() {
        // GIVEN
        ConstructorItem constructorItem = createConstructorItem(TestClass.class);
        when(evaluatorService.checkTypesInOrder(any(), any())).thenReturn(true);
        when(evaluatorService.checkTypesInAnyOrder(any(), any())).thenReturn(true);
        when(evaluatorService.checkModifiers(anyInt(), anyInt())).thenReturn(true);

        // WHEN
        ScoredItem scoredItem = constructorItemEvaluator.evaluate(constructorItem);

        // THEN
        assertThat(scoredItem.getScore()).isEqualTo(new BigDecimal("1.0"));
        assertThat(scoredItem.getItem()).isEqualTo(constructorItem);

        Map<SyntaxElement, Boolean> checkedElements = scoredItem.getCheckedElements();
        assertThat(checkedElements.keySet()).containsExactlyInAnyOrder(EXISTENCE, MODIFIERS, EXCEPTIONS);
        assertTrue(checkedElements.get(EXISTENCE));
        assertTrue(checkedElements.get(MODIFIERS));
        assertTrue(checkedElements.get(EXCEPTIONS));
    }

    @Test
    public void checkDefaultConstructorWithIncorrectModifiers() {
        // GIVEN
        ConstructorItem constructorItem = createConstructorItem(TestClass.class);
        when(evaluatorService.checkTypesInOrder(any(), any())).thenReturn(true);
        when(evaluatorService.checkTypesInAnyOrder(any(), any())).thenReturn(true);
        when(evaluatorService.checkModifiers(anyInt(), anyInt())).thenReturn(false);

        // WHEN
        ScoredItem scoredItem = constructorItemEvaluator.evaluate(constructorItem);

        // THEN
        assertThat(scoredItem.getScore()).isEqualTo(new BigDecimal("0.67"));
        assertThat(scoredItem.getItem()).isEqualTo(constructorItem);

        Map<SyntaxElement, Boolean> checkedElements = scoredItem.getCheckedElements();
        assertThat(checkedElements.keySet()).containsExactlyInAnyOrder(EXISTENCE, MODIFIERS, EXCEPTIONS);
        assertTrue(checkedElements.get(EXISTENCE));
        assertFalse(checkedElements.get(MODIFIERS));
        assertTrue(checkedElements.get(EXCEPTIONS));
    }

    @Test
    public void checkConstructorDoesNotExist() {
        // GIVEN
        ConstructorItem constructorItem = createConstructorItem(TestClass.class);
        when(evaluatorService.checkTypesInOrder(any(), any())).thenReturn(false);

        // WHEN
        ScoredItem scoredItem = constructorItemEvaluator.evaluate(constructorItem);

        // THEN
        assertThat(scoredItem.getScore()).isEqualTo(new BigDecimal(0));
        assertThat(scoredItem.getItem()).isEqualTo(constructorItem);

        Map<SyntaxElement, Boolean> checkedElements = scoredItem.getCheckedElements();
        assertThat(checkedElements.keySet()).containsExactlyInAnyOrder(EXISTENCE);
        assertFalse(checkedElements.get(EXISTENCE));
    }

    @Test
    public void checkParameterizedConstructor() {
        // GIVEN
        ConstructorItem constructorItem = createConstructorItem(TestClass1.class);
        when(evaluatorService.checkTypesInOrder(any(), any())).thenReturn(true);
        when(evaluatorService.checkTypesInAnyOrder(any(), any())).thenReturn(true);
        when(evaluatorService.checkModifiers(anyInt(), anyInt())).thenReturn(true);

        // WHEN
        ScoredItem scoredItem = constructorItemEvaluator.evaluate(constructorItem);

        // THEN
        assertThat(scoredItem.getScore()).isEqualTo(new BigDecimal("1.0"));
        assertThat(scoredItem.getItem()).isEqualTo(constructorItem);

        Map<SyntaxElement, Boolean> checkedElements = scoredItem.getCheckedElements();
        assertThat(checkedElements.keySet()).containsExactlyInAnyOrder(EXISTENCE, MODIFIERS, EXCEPTIONS);
        assertTrue(checkedElements.get(EXISTENCE));
        assertTrue(checkedElements.get(MODIFIERS));
        assertTrue(checkedElements.get(EXCEPTIONS));
    }

    @Test
    public void checkParameterizedConstructorWithIncorrectModifier() {
        // GIVEN
        ConstructorItem constructorItem = createConstructorItem(TestClass1.class);
        when(evaluatorService.checkTypesInOrder(any(), any())).thenReturn(true);
        when(evaluatorService.checkTypesInAnyOrder(any(), any())).thenReturn(true);
        when(evaluatorService.checkModifiers(anyInt(), anyInt())).thenReturn(false);

        // WHEN
        ScoredItem scoredItem = constructorItemEvaluator.evaluate(constructorItem);

        // THEN
        assertThat(scoredItem.getScore()).isEqualTo(new BigDecimal("0.67"));
        assertThat(scoredItem.getItem()).isEqualTo(constructorItem);

        Map<SyntaxElement, Boolean> checkedElements = scoredItem.getCheckedElements();
        assertThat(checkedElements.keySet()).containsExactlyInAnyOrder(EXISTENCE, MODIFIERS, EXCEPTIONS);
        assertTrue(checkedElements.get(EXISTENCE));
        assertFalse(checkedElements.get(MODIFIERS));
        assertTrue(checkedElements.get(EXCEPTIONS));
    }

    @SneakyThrows
    private ConstructorItem createConstructorItem(Class<?> testClass) {
        Constructor<?> constructor = Arrays.stream(testClass.getDeclaredConstructors())
                .findFirst()
                .orElseThrow(NoSuchMethodException::new);
        return itemFactory.createItem(constructor.getAnnotation(ConstructorCheck.class), constructor);
    }

    public static class TestClass {
        @ConstructorCheck
        public TestClass() {
        }
    }

    public static class TestClass1 {
        @ConstructorCheck
        public TestClass1(int i) throws Exception {
        }
    }
}
