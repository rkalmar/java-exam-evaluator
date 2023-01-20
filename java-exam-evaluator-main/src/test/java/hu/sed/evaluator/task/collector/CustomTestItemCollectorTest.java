package hu.sed.evaluator.task.collector;

import hu.sed.evaluator.annotation.semantic.CustomTest;
import hu.sed.evaluator.annotation.syntax.ConstructorCheck;
import hu.sed.evaluator.annotation.syntax.SkipCheck;
import hu.sed.evaluator.item.ItemFactory;
import hu.sed.evaluator.item.semantic.TestItem;
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

public class CustomTestItemCollectorTest {

    @Mock
    ItemFactory itemFactory;

    CustomTestItemCollector customTestItemCollector;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        customTestItemCollector = new CustomTestItemCollector(itemFactory);

        when(itemFactory.createItem(any(ConstructorCheck.class), any(Constructor.class)))
                .thenReturn(ConstructorItem.builder()
                        .name(String.valueOf(new Random().nextInt(100)))
                        .build());
    }

    @Test
    public void collectCustomTests() {
        // WHEN
        List<TestItem> items = customTestItemCollector.collectItems(TestClass.class);

        // THEN
        assertThat(items).hasSize(1);
        verify(itemFactory, times(1)).createTestItem(any());
    }


    public static class TestClass {
        @CustomTest(testClass = TestClass.class)
        public void testMethod() {
        }

        @SkipCheck
        @CustomTest(testClass = TestClass.class)
        public void testMethod(int i) {
            System.out.println(i);
        }
    }
}
