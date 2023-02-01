package hu.sed.evaluator.task;

import lombok.Builder;
import lombok.experimental.UtilityClass;

import java.math.BigDecimal;
import java.math.RoundingMode;

@UtilityClass
public class CalculationUtils {

    public DistributedScore distributeScore(double maxScore, int itemCount) {
        BigDecimal itemCountBd = BigDecimal.valueOf(itemCount);
        BigDecimal subItemCount = itemCountBd.subtract(BigDecimal.ONE);

        BigDecimal maxScoreBd = divide(BigDecimal.valueOf(maxScore), BigDecimal.ONE);

        BigDecimal subItemScore = subItemCount.equals(BigDecimal.ZERO) ? BigDecimal.ZERO : divide(maxScoreBd, itemCountBd);
        BigDecimal mainItemScore = maxScoreBd.subtract(
                subItemScore.multiply(subItemCount)
        );

        // mainScore is expected to be bigger than subScore
        BigDecimal incrementBy = BigDecimal.valueOf(0.1);
        BigDecimal increaseBy = incrementBy;
        while (subItemScore.doubleValue() > mainItemScore.doubleValue()) {
            subItemScore = divide(maxScoreBd, itemCountBd.add(increaseBy));
            mainItemScore = maxScoreBd.subtract(
                    subItemScore.multiply(subItemCount)
            );
            increaseBy = increaseBy.add(incrementBy);
        }

        return DistributedScore.builder()
                .itemCount(itemCountBd)
                .maxScore(maxScoreBd)
                .mainScore(mainItemScore)
                .subScore(subItemScore)
                .build();
    }

    public BigDecimal divide(BigDecimal dividend, BigDecimal divisor) {
        return dividend.divide(divisor, 2, RoundingMode.HALF_DOWN);
    }

    @Builder
    public static class DistributedScore {
        BigDecimal maxScore;
        BigDecimal itemCount;
        BigDecimal mainScore;
        BigDecimal subScore;

        public boolean isProperlyDistributed() {
            BigDecimal subItemCount = itemCount.subtract(BigDecimal.ONE);
            BigDecimal subItemSumScore = subItemCount.multiply(subScore);
            BigDecimal result = subItemSumScore.add(mainScore);

            return result.equals(maxScore) && mainScore() >= subScore();
        }

        public double mainScore() {
            return mainScore.doubleValue();
        }

        public double subScore() {
            return subScore.doubleValue();
        }
    }
}
