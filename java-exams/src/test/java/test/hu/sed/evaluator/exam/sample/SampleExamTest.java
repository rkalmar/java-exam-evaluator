package test.hu.sed.evaluator.exam.sample;

import org.junit.jupiter.api.Test;
import test.hu.sed.evaluator.exam.ExamTestEvaluatorBase;

public class SampleExamTest extends ExamTestEvaluatorBase {

    public SampleExamTest() {
        super(Tests.class);
    }

    @Test
    public void test() {
        this.callAllTestMethod();
    }

    @Test
    public void myFirstTest() {
        this.callTestMethod("myFirstTest");
    }
}
