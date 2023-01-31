package test.hu.sed.evaluator.exam.sample;

import hu.sed.evaluator.annotation.test.BeforeEach;
import hu.sed.evaluator.annotation.test.ExamTest;
import hu.sed.evaluator.annotation.test.Setup;
import hu.sed.evaluator.exam.sample.Jarmu;
import lombok.extern.slf4j.Slf4j;
import test.hu.sed.evaluator.TestUtils;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
public class SampleJarmuTests {

    Jarmu jarmu;

    @Setup
    public void setupMethod() {
        log.info("Setup this test..");
    }

    @BeforeEach
    public void clearStateBeforeEach() {
        log.info("Clearing out previous test state..");
        int gyartasiEv = 2020;
        int sebesseg = 100;
        double fogyasztas = 7.5;
        jarmu = TestUtils.createInstanceFromAbstractClass(Jarmu.class,
                new Class[]{int.class, int.class, double.class},
                new Object[]{gyartasiEv, sebesseg, fogyasztas});
    }

    @ExamTest
    public void hasMuszakiTest() {
        int gyartasiEv = 2000;
        double fogyasztas = 10;
        jarmu = TestUtils.createInstanceFromAbstractClass(Jarmu.class,
                new Class[]{int.class, int.class, double.class},
                new Object[]{gyartasiEv, 0, fogyasztas});
        assertThat(jarmu.muszaki()).isTrue();
    }

    @ExamTest
    public void hasMuszakiTest2() {
        int gyartasiEv = 1999;
        double fogyasztas = 7.9;
        jarmu = TestUtils.createInstanceFromAbstractClass(Jarmu.class,
                new Class[]{int.class, int.class, double.class},
                new Object[]{gyartasiEv, 0, fogyasztas});
        assertThat(jarmu.muszaki()).isTrue();
    }

    @ExamTest
    public void doesNotHaveMuszakiTest() {
        int gyartasiEv = 1999;
        double fogyasztas = 8.0;
        jarmu = TestUtils.createInstanceFromAbstractClass(Jarmu.class,
                new Class[]{int.class, int.class, double.class},
                new Object[]{gyartasiEv, 0, fogyasztas});
        assertThat(jarmu.muszaki()).isFalse();
    }

}
