package test.hu.sed.evaluator.exam.sample;

import org.junit.jupiter.api.Test;
import test.hu.sed.evaluator.exam.ExamTestEvaluatorBase;

public class SampleExamTest extends ExamTestEvaluatorBase {

    public SampleExamTest() {
        super(SampleTests.class);
    }


    @Test
    public void checkDefaultConstructor() {
        this.callTestMethod("checkDefaultConstructor");
    }
}
