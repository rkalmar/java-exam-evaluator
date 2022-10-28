package hu.sed.evaluator.task.item;

import hu.sed.evaluator.annotation.syntax.MethodCheck;
import hu.sed.evaluator.annotation.syntax.TypeCheck;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

public class TypeItemFactoryTest {

    ItemFactory itemFactory = new ItemFactory();

    @Test
    public void testSimpleClass() {
        // GIVEN
        String clazzName = "TestClass";
        Class<?> aClass = getClassByName(clazzName);

        // WHEN
        itemFactory.createTypeItem(aClass.getAnnotation(TypeCheck.class), aClass);
    }

    private Class<?> getClassByName(String clazzName) {
        return Arrays.stream(TypeItemFactoryTest.class.getDeclaredClasses())
                .filter(clazz -> clazz.getSimpleName().equals(clazzName))
                .findFirst()
                .orElseThrow();
    }


    @TypeCheck
    private static class TestClass {
    }
}
