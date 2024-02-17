package hu.sed.evaluator.task.collector;

import hu.sed.evaluator.annotation.syntax.MethodCheck;
import hu.sed.evaluator.annotation.syntax.SkipCheck;
import hu.sed.evaluator.item.ItemFactory;
import hu.sed.evaluator.item.syntax.MethodItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class MethodItemCollectorTest {

    @Mock
    ItemFactory itemFactory;

    MethodItemCollector methodItemCollector;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        methodItemCollector = new MethodItemCollector(itemFactory);

        when(itemFactory.createItem(any(MethodCheck.class), any(Method.class)))
                .thenReturn(MethodItem.builder()
                        .name(String.valueOf(new Random().nextInt(100)))
                        .build());
    }

    @Test
    public void collectOnlyAnnotatedTest() {
        // WHEN
        List<MethodItem> items =
                methodItemCollector.collectItems(TestClass.class);

        // THEN
        assertThat(items).hasSize(1);
        verify(itemFactory, times(1)).createItem(any(MethodCheck.class), any());
    }

    @Test
    public void collectAllTest() {
        // WHEN
        List<MethodItem> items =
                new ArrayList<>(methodItemCollector.collectItems(TestClass.class));
        items.addAll(methodItemCollector.collectUnannotatedItems(TestClass.class));

        // THEN
        assertThat(items).hasSize(2);
        verify(itemFactory, times(2)).createItem(any(MethodCheck.class), any());
    }

    public static class TestClass {
        @MethodCheck
        public void testMethod() {
        }

        public void testMethod(int i) {
            System.out.println(i);
        }

        @SkipCheck
        public void testMethod(long i) {
            System.out.println(i);
        }
    }
}
