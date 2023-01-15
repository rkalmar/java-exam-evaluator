package hu.sed.evaluator.task.item;

import hu.sed.evaluator.annotation.syntax.FieldCheck;
import hu.sed.evaluator.annotation.syntax.TypeCheck;
import hu.sed.evaluator.item.ItemFactory;
import hu.sed.evaluator.item.element.TypeDefinition;
import hu.sed.evaluator.item.syntax.FieldItem;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FieldItemFactoryTest {

    ItemFactory itemFactory = new ItemFactory();

    @SneakyThrows
    @Test
    public void testCreatePrimitiveFieldItem() {
        // GIVEN
        String fieldName = "condition";
        Field condition = TestClass.class.getDeclaredField(fieldName);

        // WHEN
        FieldItem fieldItem = itemFactory.createItem(condition.getAnnotation(FieldCheck.class), condition);

        // THEN
        assertEquals(fieldName, fieldItem.getName());
        assertThat(fieldItem.getReadableModifiers()).isEqualTo("private static");

        TypeDefinition type = fieldItem.getType();
        assertThat(type.getType()).isEqualTo("boolean");
        assertThat(type.getGenericTypes().length).isEqualTo(0);
        assertThat(fieldItem.getScore()).isEqualTo(1);
        assertTrue(fieldItem.isCheckModifiers());
    }

    @SneakyThrows
    @Test
    public void testCreateStringFieldItem() {
        // GIVEN
        String fieldName = "myText";
        Field textField = TestClass.class.getDeclaredField(fieldName);

        // WHEN
        FieldItem fieldItem = itemFactory.createItem(textField.getAnnotation(FieldCheck.class), textField);

        // THEN
        assertThat(fieldItem.getName()).isEqualTo(fieldName);
        assertThat(fieldItem.getReadableModifiers()).isEqualTo("protected");
        TypeDefinition type = fieldItem.getType();
        assertThat(type.getType()).isEqualTo(String.class.getCanonicalName());
        assertThat(type.getGenericTypes().length).isEqualTo(0);
        assertThat(fieldItem.getScore()).isEqualTo(10);
        assertFalse(fieldItem.isCheckModifiers());
    }

    @SneakyThrows
    @Test
    public void testCreateGenericFieldItem() {
        // GIVEN
        String fieldName = "testList";
        Field genericField = TestClass.class.getDeclaredField(fieldName);

        // WHEN
        FieldItem fieldItem = itemFactory.createItem(genericField.getAnnotation(FieldCheck.class), genericField);

        // THEN
        assertThat(fieldItem.getName()).isEqualTo(fieldName);
        assertThat(fieldItem.getReadableModifiers()).isEqualTo("private");

        TypeDefinition type = fieldItem.getType();
        assertThat(type.getType()).isEqualTo(List.class.getCanonicalName());
        assertThat(type.getGenericTypes().length).isEqualTo(1);

        TypeDefinition genericType = type.getGenericTypes()[0];
        assertThat(genericType.getType()).isEqualTo(Double.class.getCanonicalName());
        assertThat(genericType.getGenericTypes().length).isEqualTo(0);
    }

    @SneakyThrows
    @Test
    public void testCreateGenericFieldItem2() {
        // GIVEN
        String fieldName = "testList2";
        Field genericField = TestClass.class.getDeclaredField(fieldName);

        // WHEN
        FieldItem fieldItem = itemFactory.createItem(genericField.getAnnotation(FieldCheck.class), genericField);

        // THEN
        assertThat(fieldItem.getName()).isEqualTo(fieldName);
        assertThat(fieldItem.getReadableModifiers()).isEqualTo("private");

        TypeDefinition type = fieldItem.getType();
        assertThat(type.getType()).isEqualTo(List.class.getCanonicalName());
        assertThat(type.getGenericTypes().length).isEqualTo(1);

        type = type.getGenericTypes()[0];
        assertThat(type.getType()).isEqualTo(List.class.getCanonicalName());
        assertThat(type.getGenericTypes().length).isEqualTo(1);

        type = type.getGenericTypes()[0];
        assertThat(type.getType()).isEqualTo(Double.class.getCanonicalName());
        assertThat(type.getGenericTypes().length).isEqualTo(0);
    }


    @SneakyThrows
    @Test
    public void testGenericMapField() {
        // GIVEN
        String fieldName = "testMap";
        Field genericField = TestClass.class.getDeclaredField(fieldName);

        // WHEN
        FieldItem fieldItem = itemFactory.createItem(genericField.getAnnotation(FieldCheck.class), genericField);

        // THEN
        TypeDefinition type = fieldItem.getType();
        assertThat(type.getType()).isEqualTo(Map.class.getCanonicalName());
        assertThat(type.getGenericTypes().length).isEqualTo(2);

        TypeDefinition genericType = type.getGenericTypes()[0];
        assertThat(genericType.getType()).isEqualTo(Integer.class.getCanonicalName());
        assertThat(genericType.getGenericTypes().length).isEqualTo(0);

        genericType = type.getGenericTypes()[1];
        assertThat(genericType.getType()).isEqualTo(String.class.getCanonicalName());
        assertThat(genericType.getGenericTypes().length).isEqualTo(0);
    }


    @SneakyThrows
    @Test
    public void checkDifferentGenericTypesAreNotEqual() {
        // GIVEN
        Field mapField1 = TestClass.class.getDeclaredField("testMap");
        Field mapField2 = TestClass.class.getDeclaredField("testMap2");

        // WHEN
        FieldItem mapFieldItem1 = itemFactory.createItem(mapField1.getAnnotation(FieldCheck.class), mapField1);
        FieldItem mapFieldItem1a = itemFactory.createItem(mapField1.getAnnotation(FieldCheck.class), mapField1);
        FieldItem mapFieldItem2 = itemFactory.createItem(mapField2.getAnnotation(FieldCheck.class), mapField2);

        // THEN
        assertThat(mapFieldItem1).isNotEqualTo(mapFieldItem2);
        assertThat(mapFieldItem1).isEqualTo(mapFieldItem1a);
    }

    private static final class TestClass {
        @FieldCheck
        private static boolean condition;

        @FieldCheck(checkModifiers = false, score = 10)
        protected String myText;

        @FieldCheck
        private List<Double> testList;

        @FieldCheck
        private List<List<Double>> testList2;

        @FieldCheck
        private Map<Integer, String> testMap;

        @FieldCheck
        private Map<String, Integer> testMap2;
    }
}
