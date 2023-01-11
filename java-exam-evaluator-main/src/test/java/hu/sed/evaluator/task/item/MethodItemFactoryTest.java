package hu.sed.evaluator.task.item;

import hu.sed.evaluator.annotation.syntax.ConstructorCheck;
import hu.sed.evaluator.annotation.syntax.MethodCheck;
import hu.sed.evaluator.item.ItemFactory;
import hu.sed.evaluator.item.element.TypeDefinition;
import hu.sed.evaluator.item.syntax.MethodItem;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@NoArgsConstructor
public class MethodItemFactoryTest {

    ItemFactory itemFactory = new ItemFactory();

    @MethodCheck(checkExceptions = false, checkOverride = false, maxPoint = 8)
    public static void testMethod(int x, Double z) {
        System.out.println(x + z);
    }

    @SneakyThrows
    @DisplayName("Test a simple method with no exception and void return value")
    @Test
    public void testMethod1() {
        // GIVEN
        String methodName = "testMethod";
        Method method = getMethodByName(methodName);

        // WHEN
        MethodItem methodItem = itemFactory.createItem(method.getAnnotation(MethodCheck.class), method);

        // THEN
        assertThat(methodItem.getReadableModifiers()).isEqualTo("public static");
        assertThat(methodItem.getName()).isEqualTo(methodName);
        assertThat(methodItem.isCheckModifiers()).isTrue();
        assertThat(methodItem.isCheckOverride()).isFalse();
        assertThat(methodItem.isCheckExceptions()).isFalse();
        assertThat(methodItem.getPoints()).isEqualTo(8);
        assertThat(methodItem.isConstructor()).isFalse();
        assertThat(methodItem.getContainerClass()).isEqualTo("hu.sed.evaluator.task.item.MethodItemFactoryTest");

        // exceptions
        assertThat(methodItem.getExceptions()).isEmpty();

        // params
        assertThat(methodItem.getParameters().length).isEqualTo(2);

        TypeDefinition parameter = methodItem.getParameters()[0];
        assertThat(parameter.getType()).isEqualTo("int");
        assertThat(parameter.getGenericTypes()).isEmpty();

        parameter = methodItem.getParameters()[1];
        assertThat(parameter.getType()).isEqualTo(Double.class.getCanonicalName());
        assertThat(parameter.getGenericTypes()).isEmpty();

        // return value
        TypeDefinition returnType = methodItem.getReturnType();
        assertThat(returnType.getType()).isEqualTo("void");
        assertThat(returnType.getGenericTypes()).isEmpty();
    }

    @SneakyThrows
    @DisplayName("Test a method with generic param/return value and with exception")
    @Test
    public void testMethod2() {
        // GIVEN
        String methodName = "testMethod2";
        Method method = getMethodByName(methodName);

        // WHEN
        MethodItem methodItem = itemFactory.createItem(method.getAnnotation(MethodCheck.class), method);

        // THEN
        assertThat(methodItem.getReadableModifiers()).isEqualTo("private final");
        assertThat(methodItem.getName()).isEqualTo(methodName);
        assertThat(methodItem.isCheckModifiers()).isTrue();
        assertThat(methodItem.isCheckOverride()).isTrue();
        assertThat(methodItem.isCheckExceptions()).isTrue();
        assertThat(methodItem.getPoints()).isEqualTo(1);
        assertThat(methodItem.isConstructor()).isFalse();
        assertThat(methodItem.getContainerClass()).isEqualTo("hu.sed.evaluator.task.item.MethodItemFactoryTest");

        // exceptions
        assertThat(methodItem.getExceptions().length).isEqualTo(1);
        TypeDefinition exception = methodItem.getExceptions()[0];
        assertThat(exception.getType()).isEqualTo(TestException.class.getCanonicalName());
        assertThat(exception.getGenericTypes()).isEmpty();

        // params
        assertThat(methodItem.getParameters().length).isEqualTo(1);

        TypeDefinition parameter = methodItem.getParameters()[0];
        assertThat(parameter.getType()).isEqualTo(List.class.getCanonicalName());
        assertThat(parameter.getGenericTypes().length).isEqualTo(1);
        TypeDefinition genericType = parameter.getGenericTypes()[0];
        assertThat(genericType.getType()).isEqualTo(String.class.getCanonicalName());
        assertThat(genericType.getGenericTypes()).isEmpty();

        // return value
        TypeDefinition returnType = methodItem.getReturnType();
        assertThat(returnType.getType()).isEqualTo(List.class.getCanonicalName());
        assertThat(returnType.getGenericTypes().length).isEqualTo(1);
        TypeDefinition returnGenericType = returnType.getGenericTypes()[0];
        assertThat(returnGenericType.getType()).isEqualTo(String.class.getCanonicalName());
        assertThat(returnGenericType.getGenericTypes()).isEmpty();
    }

    @SneakyThrows
    @DisplayName("Test constructor method with a single param")
    @Test
    public void testMethod3() {
        // GIVEN
        Constructor<?> constructor = Arrays.stream(TestClass.class.getConstructors())
                .filter(constructor1 -> constructor1.isAnnotationPresent(ConstructorCheck.class))
                .findFirst()
                .orElseThrow();

        // WHEN
        MethodItem constructorMethod = itemFactory.createItem(constructor.getAnnotation(ConstructorCheck.class), constructor);

        // THEN
        assertThat(constructorMethod.getReadableModifiers()).isEqualTo("public");
        assertThat(constructorMethod.isCheckModifiers()).isTrue();
        assertThat(constructorMethod.isCheckOverride()).isFalse();
        assertThat(constructorMethod.isCheckExceptions()).isTrue();
        assertThat(constructorMethod.getPoints()).isEqualTo(1);
        assertThat(constructorMethod.isConstructor()).isTrue();
        assertThat(constructorMethod.getContainerClass()).isEqualTo("hu.sed.evaluator.task.item.MethodItemFactoryTest");

        // exceptions
        assertThat(constructorMethod.getExceptions().length).isEqualTo(1);
        TypeDefinition exception = constructorMethod.getExceptions()[0];
        assertThat(exception.getType()).isEqualTo(Exception.class.getCanonicalName());
        assertThat(exception.getGenericTypes()).isEmpty();

        // params
        assertThat(constructorMethod.getParameters().length).isEqualTo(1);

        TypeDefinition parameter = constructorMethod.getParameters()[0];
        assertThat(parameter.getType()).isEqualTo(ItemFactory.class.getCanonicalName());
        assertThat(parameter.getGenericTypes().length).isEqualTo(0);
    }

    private Method getMethodByName(String methodName) {
        return Arrays.stream(MethodItemFactoryTest.class.getDeclaredMethods())
                .filter(method1 -> method1.getName().equals(methodName))
                .findFirst()
                .orElseThrow();
    }

    @MethodCheck
    private final List<String> testMethod2(List<String> param) throws TestException {
        throw new TestException();
    }

    private static class TestClass {
        @ConstructorCheck
        public TestClass(ItemFactory itemFactory) throws Exception {
        }
    }


    public static class TestException extends Exception {
    }
}
