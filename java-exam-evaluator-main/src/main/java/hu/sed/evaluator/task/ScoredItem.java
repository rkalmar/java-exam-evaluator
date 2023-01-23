package hu.sed.evaluator.task;

import hu.sed.evaluator.item.ScorableItem;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.util.HashMap;
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

    public final double getScore() {
        if (checkedElements.size() == 0) {
            return 0;
        }
        long successfulCount = checkedElements.values().stream().filter(value -> value).count();
        return ((double) getItem().getScore() / checkedElements.size()) * successfulCount;
    }

    public final int getMaxScore() {
        return item.getScore();
    }

    public String identifier() {
        return item.identifier();
    }
}