package hu.sed.evaluator.task.evaluator;

import hu.sed.evaluator.annotation.semantic.CustomTestContants;
import hu.sed.evaluator.annotation.test.ExamTest;
import hu.sed.evaluator.item.semantic.TestItem;
import hu.sed.evaluator.task.evaluator.semantic.ScoredSemanticItem;
import hu.sed.evaluator.task.evaluator.semantic.TestEvaluator;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Java6Assertions.assertThat;

public class TestEvaluatorTest {

    TestEvaluator testEvaluator = new TestEvaluator();

    @Test
    public void testSimpleMethod() {
        // GIVEN
        TestItem testItem = createTestItem("simpleTestMethod");
        // WHEN
        ScoredSemanticItem scoredItem = testEvaluator.evaluate(testItem);

        // THEN
        assertThat(scoredItem.getScore()).isEqualTo(new BigDecimal("1.0"));
    }

    @Test
    public void testSimpleTestFailure() {
        // GIVEN
        TestItem testItem = createTestItem("failedTestMethod");

        // WHEN
        ScoredSemanticItem scoredItem = testEvaluator.evaluate(testItem);

        // THEN
        assertThat(scoredItem.getScore()).isEqualTo(new BigDecimal(0));
    }

    @Test
    public void testClassNotFound() {
        // GIVEN
        TestItem testItem = createTestItem("failedTestMethod");
        testItem.setTestClass("unknown.Unknown");

        // WHEN
        ScoredSemanticItem scoredItem = testEvaluator.evaluate(testItem);

        // THEN
        assertThat(scoredItem.getScore()).isEqualTo(new BigDecimal(0));
    }

    @Test
    public void testClassCannotBeInstantiated() {
        // GIVEN
        TestItem testItem = createTestItem("failedTestMethod");
        testItem.setTestClass(TestClass2.class.getName());

        // WHEN
        ScoredSemanticItem scoredItem = testEvaluator.evaluate(testItem);

        // THEN
        assertThat(scoredItem.getScore()).isEqualTo(new BigDecimal(0));
    }

    @Test
    public void cannotExecuteTestMethod() {
        // GIVEN
        TestItem testItem = createTestItem("nonExistingMethod");

        // WHEN
        ScoredSemanticItem scoredItem = testEvaluator.evaluate(testItem);

        // THEN
        assertThat(scoredItem.getScore()).isEqualTo(new BigDecimal(0));
    }

    @Test
    public void callAllTestMethod() {
        // GIVEN
        TestItem testItem = createTestItem(CustomTestContants.ALL_TEST);

        // WHEN
        ScoredSemanticItem scoredItem = testEvaluator.evaluate(testItem);

        // THEN
        assertThat(scoredItem.getScore()).isEqualTo(new BigDecimal("0.50"));
    }

    private TestItem createTestItem(String... testMethods) {
        return TestItem.builder()
                .testClass(TestClass.class.getName())
                .testMethods(testMethods)
                .score(1)
                .build();
    }


    public static class TestClass {
        @ExamTest
        public void simpleTestMethod() {
            System.out.println("This is just a simple test.");
        }

        @ExamTest
        public void failedTestMethod() {
            assert false;
        }
    }

    public static class TestClass2  {
        private TestClass2() {
        }
    }

}
