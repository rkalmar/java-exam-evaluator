package hu.sed.evaluator.task.collector;

import hu.sed.evaluator.annotation.syntax.ConstructorCheck;
import hu.sed.evaluator.annotation.syntax.SkipCheck;
import hu.sed.evaluator.item.ItemFactory;
import hu.sed.evaluator.item.syntax.ConstructorItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.lang.reflect.Constructor;
import java.util.List;
import java.util.Random;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ConstructorItemCollectorTest {

    @Mock
    ItemFactory itemFactory;

    ConstructorItemCollector constructorItemCollector;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        constructorItemCollector = new ConstructorItemCollector(itemFactory);

        when(itemFactory.createItem(any(ConstructorCheck.class), any(Constructor.class)))
                .thenReturn(ConstructorItem.builder()
                        .name(String.valueOf(new Random().nextInt(100)))
                        .build());
    }

    @Test
    public void collectOnlyAnnotatedTest() {
        // WHEN
        List<ConstructorItem> items =
                constructorItemCollector.collectItems(TestClass.class);

        // THEN
        assertThat(items).hasSize(1);
        verify(itemFactory, times(1)).createItem(any(ConstructorCheck.class), any());
    }

    @Test
    public void collectAllTest() {
        // WHEN
        List<ConstructorItem> items =
                constructorItemCollector.collectItems(TestClass.class);
        items.addAll(constructorItemCollector.collectUnannotatedItems(TestClass.class));

        // THEN
        assertThat(items).hasSize(2);
        verify(itemFactory, times(2)).createItem(any(ConstructorCheck.class), any());
    }

    public static class TestClass {
        @ConstructorCheck
        public TestClass() {
        }

        public TestClass(int i) {
            System.out.println(i);
        }

        @SkipCheck
        public TestClass(long i) {
            System.out.println(i);
        }
    }
}
