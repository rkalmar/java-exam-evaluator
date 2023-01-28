package hu.sed.evaluator.task.item;

import hu.sed.evaluator.annotation.syntax.TypeCheck;
import hu.sed.evaluator.item.ItemFactory;
import hu.sed.evaluator.item.element.TypeDefinition;
import hu.sed.evaluator.item.syntax.TypeItem;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
        assertThat(typeItem.isCheckParentClazz()).isTrue();
        assertThat(typeItem.getScore()).isEqualTo(5);
        TypeDefinition parentClazz = typeItem.getParentClazz();
        assertThat(parentClazz.getType()).isEqualTo("java.lang.Object");
        assertThat(parentClazz.getGenericTypes()).isEmpty();
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
        TypeDefinition parentClazz = typeItem.getParentClazz();
        assertThat(parentClazz.getType()).isEqualTo("java.lang.Object");
        assertThat(parentClazz.getGenericTypes()).isEmpty();
        assertThat(typeItem.getImplementedInterfaces()).isNotNull();
        assertThat(typeItem.getImplementedInterfaces().length).isEqualTo(1);
        TypeDefinition implementedInterface = typeItem.getImplementedInterfaces()[0];
        assertThat(implementedInterface.getType()).isEqualTo("hu.sed.evaluator.task.item.TypeItemFactoryTest$TestInterface");
        assertThat(implementedInterface.getGenericTypes()).isEmpty();
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
        TypeDefinition parentClazz = typeItem.getParentClazz();
        assertThat(parentClazz.getType()).isEqualTo("hu.sed.evaluator.task.item.TypeItemFactoryTest$AbstractTestClass");
        assertThat(parentClazz.getGenericTypes()).isEmpty();
        assertThat(typeItem.getImplementedInterfaces()).isNotNull();
        assertThat(typeItem.getImplementedInterfaces()).isEmpty();
        assertThat(typeItem.getItems()).isNull();
    }

    @Test
    public void testClassWithGenericInterface() {
        // GIVEN
        String clazzName = "MyList";
        Class<?> aClass = getClassByName(clazzName);

        // WHEN
        TypeItem typeItem = itemFactory.createItem(aClass.getAnnotation(TypeCheck.class), aClass);

        // THEN
        assertThat(typeItem.getReadableModifiers()).isEqualTo("private abstract static");
        assertThat(typeItem.isCheckModifiers()).isTrue();
        assertThat(typeItem.isCheckInterfaces()).isTrue();
        assertThat(typeItem.isCheckParentClazz()).isTrue();
        assertThat(typeItem.getScore()).isEqualTo(1);

        TypeDefinition parentClazz = typeItem.getParentClazz();
        assertThat(parentClazz.getType()).isEqualTo("java.lang.Object");
        assertThat(parentClazz.getGenericTypes()).isEmpty();

        assertThat(typeItem.getImplementedInterfaces()).hasSize(1);
        TypeDefinition implementedInterface = typeItem.getImplementedInterfaces()[0];
        assertThat(implementedInterface.getType()).isEqualTo("java.util.List");
        assertThat(implementedInterface.getGenericTypes()).hasSize(1);
        TypeDefinition genericTypeOfInterface = implementedInterface.getGenericTypes()[0];
        assertThat(genericTypeOfInterface.getType()).isEqualTo("hu.sed.evaluator.task.item.TypeItemFactoryTest$TestClass");
        assertThat(genericTypeOfInterface.getGenericTypes()).isEmpty();

        assertThat(typeItem.getItems()).isNull();
    }

    @Test
    public void testClassWithGenericParentClass() {
        // GIVEN
        String clazzName = "MyArrayList";
        Class<?> aClass = getClassByName(clazzName);

        // WHEN
        TypeItem typeItem = itemFactory.createItem(aClass.getAnnotation(TypeCheck.class), aClass);

        // THEN
        assertThat(typeItem.getReadableModifiers()).isEqualTo("private abstract static");
        assertThat(typeItem.isCheckModifiers()).isTrue();
        assertThat(typeItem.isCheckInterfaces()).isTrue();
        assertThat(typeItem.isCheckParentClazz()).isTrue();
        assertThat(typeItem.getScore()).isEqualTo(1);

        TypeDefinition parentClazz = typeItem.getParentClazz();
        assertThat(parentClazz.getType()).isEqualTo("java.util.ArrayList");
        assertThat(parentClazz.getGenericTypes()).hasSize(1);
        TypeDefinition genericTypeOfParentClass = parentClazz.getGenericTypes()[0];
        assertThat(genericTypeOfParentClass.getType()).isEqualTo("hu.sed.evaluator.task.item.TypeItemFactoryTest$TestClass");
        assertThat(genericTypeOfParentClass.getGenericTypes()).isEmpty();

        assertThat(typeItem.getImplementedInterfaces()).hasSize(0);

        assertThat(typeItem.getItems()).isNull();
    }

    @Test
    public void testSimpleInterface() {
        // GIVEN
        String clazzName = "TestInterface";
        Class<?> aClass = getClassByName(clazzName);

        // WHEN
        TypeItem typeItem = itemFactory.createItem(aClass.getAnnotation(TypeCheck.class), aClass);

        // THEN
        assertThat(typeItem.getReadableModifiers()).isEqualTo("private abstract static interface");
        assertThat(typeItem.isCheckParentClazz()).isFalse();
        assertThat(typeItem.getImplementedInterfaces()).isEmpty();
        assertThat(typeItem.isInterfaze()).isTrue();
    }

    private Class<?> getClassByName(String clazzName) {
        return Arrays.stream(TypeItemFactoryTest.class.getDeclaredClasses())
                .filter(clazz -> clazz.getSimpleName().equals(clazzName))
                .findFirst()
                .orElseThrow();
    }

    @TypeCheck
    private interface TestInterface {
    }

    @TypeCheck(checkInterfaces = false, score = 5)
    private static class JustAClass {
    }

    @TypeCheck
    private static abstract class AbstractTestClass implements TestInterface {
    }

    @TypeCheck
    private static class TestClass extends AbstractTestClass {
    }

    @TypeCheck
    private static abstract class MyList implements List<TestClass> {
    }

    @TypeCheck
    private static abstract class MyArrayList extends ArrayList<TestClass> {
    }
}
