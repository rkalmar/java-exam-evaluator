package test.hu.sed.evaluator.exam.sample;

import hu.sed.evaluator.exam.sample.TulNagySebesseg;
import lombok.extern.slf4j.Slf4j;
import test.hu.sed.evaluator.TestUtils;

@Slf4j
public class SampleTests {

    public void setup() {
        log.info("Setup this test..");
    }

    public void beforeEach() {
        log.info("Clearing out previous test state..");
    }

    public void checkDefaultConstructor() {
        TestUtils.checkNoDefaultConstructor(TulNagySebesseg.class);
    }
}
