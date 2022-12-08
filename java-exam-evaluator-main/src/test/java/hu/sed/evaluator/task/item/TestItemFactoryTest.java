package hu.sed.evaluator.task.item;

import hu.sed.evaluator.annotation.semantic.CustomTest;
import hu.sed.evaluator.item.ItemFactory;
import hu.sed.evaluator.item.semantic.TestItem;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

public class TestItemFactoryTest {

    public static final String MY_DESC = "my desc";
    public static final String MY_TEST_METHOD = "myTestMethod";
    public static final String MY_TEST_METHOD_2 = "myTestMethod2";

    @CustomTest(testClass = TestItemFactoryTest.class, maxPoint = 5, description = MY_DESC)
    private static String strField;

    ItemFactory itemFactory = new ItemFactory();

    @CustomTest(testClass = TestItemFactoryTest.class, method = {MY_TEST_METHOD, MY_TEST_METHOD_2})
    private static void method() {
    }

    @Test
    public void testCustomTestOnField() throws NoSuchFieldException {
        // GIVEN
        String fieldName = "strField";
        Field field = TestItemFactoryTest.class.getDeclaredField(fieldName);

        // WHEN
        TestItem testItem = itemFactory.createTestItem(field.getAnnotation(CustomTest.class));

        // THEN
        assertThat(testItem.getPoints()).isEqualTo(5);
        assertThat(testItem.getTestClass()).isEqualTo("hu.sed.evaluator.task.item.TestItemFactoryTest");
        assertThat(testItem.getTestMethods()).isNotEmpty();
        assertThat(testItem.getTestMethods().length).isEqualTo(1);
        assertThat(testItem.getTestMethods()[0]).isEqualTo("all");
        assertThat(testItem.getDescription()).isEqualTo(MY_DESC);
    }

    @Test
    public void testCustomTestOnMethod() {
        // GIVEN
        String methodName = "method";
        Method method = Arrays.stream(TestItemFactoryTest.class.getDeclaredMethods())
                .filter(method1 -> method1.getName().equals(methodName))
                .findFirst()
                .orElseThrow();

        // WHEN
        TestItem testItem = itemFactory.createTestItem(method.getAnnotation(CustomTest.class));

        // THEN
        assertThat(testItem.getPoints()).isEqualTo(1);
        assertThat(testItem.getTestClass()).isEqualTo("hu.sed.evaluator.task.item.TestItemFactoryTest");
        assertThat(testItem.getTestMethods()).isNotEmpty();
        assertThat(testItem.getTestMethods().length).isEqualTo(2);
        assertThat(testItem.getTestMethods()[0]).isEqualTo(MY_TEST_METHOD);
        assertThat(testItem.getTestMethods()[1]).isEqualTo(MY_TEST_METHOD_2);
        assertThat(testItem.getDescription()).isEmpty();
    }

}
