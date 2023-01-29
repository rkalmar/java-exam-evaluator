package test.hu.sed.evaluator.exam.sample;

import static org.assertj.core.api.Assertions.assertThat;

import hu.sed.evaluator.annotation.test.BeforeEach;
import hu.sed.evaluator.annotation.test.ExamTest;
import hu.sed.evaluator.annotation.test.Setup;
import hu.sed.evaluator.exam.sample.Auto;
import hu.sed.evaluator.exam.sample.Autopalya;
import hu.sed.evaluator.exam.sample.Jarmu;
import hu.sed.evaluator.exam.sample.Kamion;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SampleAutopalyaTests  {

    Autopalya autopalya;

    @Setup
    public void setupMethod() {
        log.info("Setup Autopalya tests..");
    }

    @BeforeEach
    public void clearStateBeforeEach() {
        log.info("Clearing out previous test state..");
        autopalya = new Autopalya("M1");
    }

    @ExamTest
    public void driveOntoSuccessfully() {
        // vehicles allowed only with speed equal or above 90 to drive onto the highway
        Jarmu jarmu = new Auto(2000, 90, 10, "toyota");
        assertThat(autopalya.felhajt(jarmu)).isTrue();
        assertThat(autopalya.jarmuvek.size()).isEqualTo(1);
        assertThat(autopalya.jarmuvek).contains(jarmu);
    }

    @ExamTest
    public void driveOntoUnsuccessful() {
        Jarmu jarmu = new Auto(2000, 89, 10, "toyota");
        assertThat(autopalya.felhajt(jarmu)).isFalse();
        assertThat(autopalya.jarmuvek.isEmpty()).isTrue();
    }

    @ExamTest
    public void speedUpTest() {
        Jarmu auto = new Auto(2000, 100, 10, "toyota");
        auto.muszaki();
        Jarmu kamion = new Kamion(2000, 10, 80, 100);
        kamion.setSebesseg(100);
        assertThat(autopalya.felhajt(auto)).isTrue();
        assertThat(autopalya.felhajt(kamion)).isTrue();

        autopalya.sebessegetNovel(10);

        for (Jarmu jarmu : autopalya.jarmuvek) {
            assertThat(jarmu.getSebesseg()).isEqualTo(110);
        }
    }
}
