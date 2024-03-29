package hu.sed.evaluator.task.evaluator;

import hu.sed.evaluator.annotation.syntax.MethodCheck;
import hu.sed.evaluator.item.ItemFactory;
import hu.sed.evaluator.item.syntax.MethodItem;
import hu.sed.evaluator.task.ScoredItem;
import hu.sed.evaluator.task.evaluator.syntax.SyntaxElement;
import hu.sed.evaluator.task.evaluator.syntax.EvaluatorService;
import hu.sed.evaluator.task.evaluator.syntax.MethodItemEvaluator;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Map;

import static hu.sed.evaluator.task.evaluator.syntax.SyntaxElement.*;
import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

public class MethodItemEvaluatorTest {

    ItemFactory itemFactory;

    @Mock
    EvaluatorService evaluatorService;

    MethodItemEvaluator methodItemEvaluator;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        itemFactory = new ItemFactory();
        methodItemEvaluator = new MethodItemEvaluator(evaluatorService, itemFactory);
    }

    @Test
    public void methodSuccessfulTest() {
        // GIVEN
        MethodItem methodItem = createMethodItem("testMethod");
        when(evaluatorService.checkType(any(), any())).thenReturn(true);
        when(evaluatorService.checkTypesInOrder(any(), any())).thenReturn(true);
        when(evaluatorService.checkTypesInAnyOrder(any(), any())).thenReturn(true);
        when(evaluatorService.checkModifiers(anyInt(), anyInt())).thenReturn(true);

        // WHEN
        ScoredItem scoredItem = methodItemEvaluator.evaluate(methodItem);

        // THEN
        assertThat(scoredItem.getScore()).isEqualTo(new BigDecimal("1.0"));
        assertThat(scoredItem.getItem()).isEqualTo(methodItem);

        Map<SyntaxElement, Boolean> checkedElements = scoredItem.getCheckedElements();
        assertThat(checkedElements.keySet()).containsExactlyInAnyOrder(EXISTENCE, MODIFIERS, EXCEPTIONS);
        assertTrue(checkedElements.get(EXISTENCE));
        assertTrue(checkedElements.get(MODIFIERS));
        assertTrue(checkedElements.get(EXCEPTIONS));
    }

    @Test
    public void methodExistsTest() {
        // GIVEN
        MethodItem methodItem = createMethodItem("testMethod");
        when(evaluatorService.checkType(any(), any())).thenReturn(true);
        when(evaluatorService.checkTypesInOrder(any(), any())).thenReturn(true);
        when(evaluatorService.checkTypesInAnyOrder(any(), any())).thenReturn(true);
        when(evaluatorService.checkModifiers(anyInt(), anyInt())).thenReturn(false);

        // WHEN
        ScoredItem scoredItem = methodItemEvaluator.evaluate(methodItem);

        // THEN
        assertThat(scoredItem.getScore()).isEqualTo(new BigDecimal("0.67"));
        assertThat(scoredItem.getItem()).isEqualTo(methodItem);

        Map<SyntaxElement, Boolean> checkedElements = scoredItem.getCheckedElements();
        assertThat(checkedElements.keySet()).containsExactlyInAnyOrder(EXISTENCE, MODIFIERS, EXCEPTIONS);
        assertTrue(checkedElements.get(EXISTENCE));
        assertFalse(checkedElements.get(MODIFIERS));
        assertTrue(checkedElements.get(EXCEPTIONS));
    }

    @Test
    public void methodDoesNotExists() {
        // GIVEN
        MethodItem methodItem = createMethodItem("testMethod");
        when(evaluatorService.checkType(any(), any())).thenReturn(false);

        // WHEN
        ScoredItem scoredItem = methodItemEvaluator.evaluate(methodItem);

        // THEN
        assertThat(scoredItem.getScore()).isEqualTo(new BigDecimal("0"));
        assertThat(scoredItem.getItem()).isEqualTo(methodItem);

        Map<SyntaxElement, Boolean> checkedElements = scoredItem.getCheckedElements();
        assertThat(checkedElements.keySet()).containsExactlyInAnyOrder(EXISTENCE);
        assertFalse(checkedElements.get(EXISTENCE));
    }

    @SneakyThrows
    private MethodItem createMethodItem(String methodName) {
        Method method = Arrays.stream(TestClass.class.getDeclaredMethods())
                .filter(method1 -> method1.getName().equals(methodName))
                .findFirst()
                .orElseThrow(NoSuchMethodException::new);
        return itemFactory.createItem(method.getAnnotation(MethodCheck.class), method);
    }

    public static class Base {
        public void testMethod() {
        }
    }

    public static class TestClass extends Base {
        @MethodCheck
        @Override
        public void testMethod() {
        }
    }
}
