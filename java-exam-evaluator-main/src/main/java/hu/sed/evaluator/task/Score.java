package hu.sed.evaluator.task;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;

@Builder
public final class Score {

    @Getter
    List<ScoredItem<?>> scoredItems;

    public BigDecimal getMaxScore() {
        BigDecimal result = BigDecimal.ZERO;
        for (ScoredItem<?> scoredItem : scoredItems) {
            result = result.add(scoredItem.getMaxScore());
        }
        return result;
    }

    public BigDecimal getScore() {
        BigDecimal result = BigDecimal.ZERO;
        for (ScoredItem<?> scoredItem : scoredItems) {
            result = result.add(scoredItem.getScore());
        }
        return result;
    }

    public double getPercentage() {
        return CalculationUtils.divide(getScore(), getMaxScore()).doubleValue();
    }


}
