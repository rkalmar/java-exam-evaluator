package test.hu.sed.evaluator.exam.sample;

import hu.sed.evaluator.annotation.test.BeforeEach;
import hu.sed.evaluator.annotation.test.Setup;
import hu.sed.evaluator.annotation.test.Test;
import hu.sed.evaluator.exam.sample.TulNagySebesseg;
import lombok.extern.slf4j.Slf4j;
import test.hu.sed.evaluator.TestUtils;

@Slf4j
public class SampleTests {

    @Setup
    public void setupMethod() {
        log.info("Setup this test..");
    }

    @BeforeEach
    public void clearStateBeforeEach() {
        log.info("Clearing out previous test state..");
    }

    @Test
    public void checkDefaultConstructor() {
        TestUtils.checkNoDefaultConstructor(TulNagySebesseg.class);
    }
}
