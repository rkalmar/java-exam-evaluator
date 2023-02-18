package test.hu.sed.evaluator.exam.sample;

import static org.assertj.core.api.Assertions.assertThat;

import hu.sed.evaluator.annotation.test.BeforeEach;
import hu.sed.evaluator.annotation.test.ExamTest;
import hu.sed.evaluator.exam.sample.Kamion;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SampleKamionTests {

    Kamion kamion;

    @BeforeEach
    public void clearStateBeforeEach() {
        log.info("Clearing out previous test state..");
        kamion = new Kamion(2010, 10, 50, 300);
    }

    @ExamTest
    public void testConstruct() {
        assertThat(kamion.getLoero()).isEqualTo(300);
        assertThat(kamion.getOsszTomeg()).isEqualTo(50);
        assertThat(kamion.getGyartasiEv()).isEqualTo(2010);
        assertThat(kamion.getSebesseg()).isEqualTo(0);
        assertThat(kamion.getFogyasztas()).isEqualTo(0);
    }

    @ExamTest
    public void testMuszaki() {
        assertThat(kamion.muszaki()).isTrue();
    }
}
