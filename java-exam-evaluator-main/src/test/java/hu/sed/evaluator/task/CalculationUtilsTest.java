package hu.sed.evaluator.task;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.of;

@Slf4j
public class CalculationUtilsTest {

    private static Stream<Arguments> distributionInputs() {
        return Stream.of(
            of(10.0, 1), of(10.0, 2), of(10.0, 3),
            of(11.0, 8), of(11.0, 3), of(11.0, 7), of(11.0, 11), of(11.0, 17), of(11.0, 23), of(11.0, 27),
            of(7.0, 8), of(7.0, 3), of(7.0, 8), of(7.0, 11), of(7.0, 17), of(7.0, 23), of(7.0, 27),
            of(3.7, 8), of(7.4, 3), of(6.1, 8), of(9.5, 11), of(6.12, 17), of(7.27, 23), of(9.57, 27)
        );
    }

    @ParameterizedTest
    @MethodSource("distributionInputs")
    public void testDistribution(double maxScore, int itemCount) {
        // WHEN
        CalculationUtils.DistributedScore distributedScore = CalculationUtils.distributeScore(maxScore, itemCount);

        // THEN
        assertThat(distributedScore.isProperlyDistributed()).isTrue();

        log.info("maxScore: {}, itemCount: {}, mainScore: {}, subScore: {}, difference {} ",
                maxScore, itemCount, distributedScore.mainScore(),
                distributedScore.subScore(), distributedScore.mainScore.subtract(distributedScore.subScore));
    }
}
