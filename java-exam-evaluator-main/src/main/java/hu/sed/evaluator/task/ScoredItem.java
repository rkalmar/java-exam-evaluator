package hu.sed.evaluator.task;

import hu.sed.evaluator.item.ScorableItem;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@SuperBuilder
@FieldDefaults(level = AccessLevel.PRIVATE)
public abstract class ScoredItem<T> {

    ScorableItem item;

    @Builder.Default
    Map<T, Boolean> checkedElements = new HashMap<>();

    public final void successfulCheck(T checkedElement) {
        addCheck(checkedElement, true);
    }

    public final void unsuccessfulCheck(T checkedElement) {
        addCheck(checkedElement, false);
    }

    public final void addCheck(T checkedElement, boolean checkResult) {
        checkedElements.put(checkedElement, checkResult);
    }

    public final BigDecimal getScore() {
        int checkedElementCount = checkedElements.size();
        if (checkedElementCount == 0 || getSuccessfulChecks().isEmpty()) {
            return BigDecimal.ZERO;
        } else if (isSuccessful()) {
            return getMaxScore();
        }

        CalculationUtils.DistributedScore distributedScore = CalculationUtils.distributeScore(getItem().getScore(), checkedElementCount);

        BigDecimal result = getMaxScore();
        int unsuccessfulCheckCount = getUnsuccessfulChecks().size();

        while ((unsuccessfulCheckCount--) > 0) {
            result = result.subtract(distributedScore.subScore);
        }

        return result;
    }

    public final BigDecimal getMaxScore() {
        return BigDecimal.valueOf(item.getScore());
    }

    public final List<T> getUnsuccessfulChecks() {
        return checkedElements.keySet().stream()
                .filter(check -> !checkedElements.get(check))
                .toList();
    }

    public final List<T> getSuccessfulChecks() {
        return checkedElements.keySet().stream()
                .filter(check -> checkedElements.get(check))
                .toList();
    }

    public boolean isSuccessful() {
        return getUnsuccessfulChecks().isEmpty();
    }

    public String identifier() {
        return item.identifier();
    }
}
