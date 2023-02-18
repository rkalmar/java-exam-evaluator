package test.hu.sed.evaluator.exam.sample;

import hu.sed.evaluator.annotation.test.ExamTest;
import hu.sed.evaluator.exam.sample.TulNagySebesseg;
import test.hu.sed.evaluator.TestUtils;

public class SampleTulNagySebessegTests {

    @ExamTest
    public void noDefaultConstructor() {
        TestUtils.checkNoDefaultConstructor(TulNagySebesseg.class);
    }
}
