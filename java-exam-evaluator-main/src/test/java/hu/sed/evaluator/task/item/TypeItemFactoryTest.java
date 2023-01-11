package hu.sed.evaluator.task.item;

import hu.sed.evaluator.annotation.syntax.TypeCheck;
import hu.sed.evaluator.item.ItemFactory;
import hu.sed.evaluator.item.syntax.TypeItem;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

public class TypeItemFactoryTest {

    ItemFactory itemFactory = new ItemFactory();

    @Test
    public void testSimpleClass() {
        // GIVEN
        String clazzName = "JustAClass";
        Class<?> aClass = getClassByName(clazzName);

        // WHEN
        TypeItem typeItem = itemFactory.createItem(aClass.getAnnotation(TypeCheck.class), aClass);

        // THEN
        assertThat(typeItem.getReadableModifiers()).isEqualTo("private static");
        assertThat(typeItem.isCheckModifiers()).isTrue();
        assertThat(typeItem.isCheckInterfaces()).isFalse();
        assertThat(typeItem.isCheckParentClazz()).isFalse();
        assertThat(typeItem.getScore()).isEqualTo(5);
        assertThat(typeItem.getParentClazz()).isEqualTo("java.lang.Object");
        assertThat(typeItem.getImplementedInterfaces()).isEmpty();
        assertThat(typeItem.getItems()).isNull();
    }

    @Test
    public void testAbstractClassWithInterface() {
        // GIVEN
        String clazzName = "AbstractTestClass";
        Class<?> aClass = getClassByName(clazzName);

        // WHEN
        TypeItem typeItem = itemFactory.createItem(aClass.getAnnotation(TypeCheck.class), aClass);

        // THEN
        assertThat(typeItem.getReadableModifiers()).isEqualTo("private abstract static");
        assertThat(typeItem.isCheckModifiers()).isTrue();
        assertThat(typeItem.isCheckInterfaces()).isTrue();
        assertThat(typeItem.isCheckParentClazz()).isTrue();
        assertThat(typeItem.getScore()).isEqualTo(1);
        assertThat(typeItem.getParentClazz()).isEqualTo("java.lang.Object");
        assertThat(typeItem.getImplementedInterfaces()).isNotNull();
        assertThat(typeItem.getImplementedInterfaces().length).isEqualTo(1);
        assertThat(typeItem.getImplementedInterfaces()[0]).isEqualTo("hu.sed.evaluator.task.item.TypeItemFactoryTest.TestInterface");
        assertThat(typeItem.getItems()).isNull();
    }

    @Test
    public void testClassWithSuperClass() {
        // GIVEN
        String clazzName = "TestClass";
        Class<?> aClass = getClassByName(clazzName);

        // WHEN
        TypeItem typeItem = itemFactory.createItem(aClass.getAnnotation(TypeCheck.class), aClass);

        // THEN
        assertThat(typeItem.getReadableModifiers()).isEqualTo("private static");
        assertThat(typeItem.isCheckModifiers()).isTrue();
        assertThat(typeItem.isCheckInterfaces()).isTrue();
        assertThat(typeItem.isCheckParentClazz()).isTrue();
        assertThat(typeItem.getScore()).isEqualTo(1);
        assertThat(typeItem.getParentClazz()).isEqualTo("hu.sed.evaluator.task.item.TypeItemFactoryTest.AbstractTestClass");
        assertThat(typeItem.getImplementedInterfaces()).isNotNull();
        assertThat(typeItem.getImplementedInterfaces()).isEmpty();
        assertThat(typeItem.getItems()).isNull();
    }

    private Class<?> getClassByName(String clazzName) {
        return Arrays.stream(TypeItemFactoryTest.class.getDeclaredClasses())
                .filter(clazz -> clazz.getSimpleName().equals(clazzName))
                .findFirst()
                .orElseThrow();
    }

    private interface TestInterface {
    }

    @TypeCheck(checkInterfaces = false, checkParentClazz = false, score = 5)
    private static class JustAClass {
    }

    @TypeCheck
    private static abstract class AbstractTestClass implements TestInterface {
    }

    @TypeCheck
    private static class TestClass extends AbstractTestClass {
    }
}
