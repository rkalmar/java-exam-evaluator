package test.hu.sed.evaluator.exam.sample;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import hu.sed.evaluator.annotation.test.BeforeEach;
import hu.sed.evaluator.annotation.test.ExamTest;
import hu.sed.evaluator.annotation.test.Setup;
import hu.sed.evaluator.exam.sample.Auto;
import hu.sed.evaluator.exam.sample.TulNagySebesseg;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SampleAutoTests {

    Auto auto;

    @Setup
    public void setupMethod() {
        log.info("Setup this test..");
    }

    @BeforeEach
    public void clearStateBeforeEach() {
        log.info("Clearing out previous test state..");
        auto = new Auto(2000, 100, 8, "toyota");
    }

    @ExamTest
    public void constructAuto() {
        Auto auto = new Auto(1933, -10, -10, "mazda");
        assertThat(auto.getMarka()).isEqualTo("mazda");
        assertThat(auto.getFogyasztas()).isEqualTo(0.0);
        assertThat(auto.getSebesseg()).isEqualTo(0);
        assertThat(auto.getMuszakis()).isFalse();
    }

    @ExamTest
    public void slowDownTest() {
        auto.lassit(20);
        assertThat(auto.getSebesseg()).isEqualTo(80);
        auto.lassit(90);
        assertThat(auto.getSebesseg()).isEqualTo(0);
    }

    @ExamTest
    public void speedUpTest() {
        // current speed is 100, without "muszaki" allowed max speed is 50.
        assertThrows(TulNagySebesseg.class, () -> auto.gyorsit(20));
        // give muszaki
        auto.muszaki();
        // ensure it has now "muszaki"
        assertThat(auto.isMuszakis()).isTrue();
        // with "muszaki", allowed max speed is 120
        assertDoesNotThrow(() -> auto.gyorsit(20));
        assertThat(auto.getSebesseg()).isEqualTo(120);
        // 121 is not allowed
        assertThrows(TulNagySebesseg.class, () -> auto.gyorsit(1));
    }
}
