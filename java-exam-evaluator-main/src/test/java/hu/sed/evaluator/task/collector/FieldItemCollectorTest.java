package hu.sed.evaluator.task.collector;

import hu.sed.evaluator.annotation.syntax.FieldCheck;
import hu.sed.evaluator.annotation.syntax.SkipCheck;
import hu.sed.evaluator.item.ItemFactory;
import hu.sed.evaluator.item.syntax.FieldItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Random;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class FieldItemCollectorTest {

    @Mock
    ItemFactory itemFactory;

    FieldItemCollector fieldItemCollector;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        fieldItemCollector = new FieldItemCollector(itemFactory);

        when(itemFactory.createItem(any(FieldCheck.class), any(Field.class)))
                .thenReturn(FieldItem.builder()
                        .name(String.valueOf(new Random().nextInt(100)))
                        .build());
    }

    @Test
    public void collectOnlyAnnotatedTest() {
        // WHEN
        List<FieldItem> items =
                fieldItemCollector.collectItems(TestClass.class);

        // THEN
        assertThat(items).hasSize(1);
        verify(itemFactory, times(1)).createItem(any(FieldCheck.class), any());
    }

    @Test
    public void collectAllTest() {
        // WHEN
        List<FieldItem> items =
                fieldItemCollector.collectItems(TestClass.class);
        items.addAll(fieldItemCollector.collectUnannotatedItems(TestClass.class));

        // THEN
        assertThat(items).hasSize(2);
        verify(itemFactory, times(2)).createItem(any(FieldCheck.class), any());
    }

    public static class TestClass {
        @FieldCheck
        int i;

        int k;

        @SkipCheck
        int z;
    }
}
