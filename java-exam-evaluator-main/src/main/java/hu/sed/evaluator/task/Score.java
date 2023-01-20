package hu.sed.evaluator.task;

import lombok.Builder;

import java.util.List;

@Builder
public final class Score {

    List<ScoredItem<?>> scoredItems;

    public double getMaxScore() {
        return scoredItems.stream().mapToDouble(ScoredItem::getMaxScore).sum();
    }

    public double getScore() {
        return scoredItems.stream().mapToDouble(ScoredItem::getScore).sum();
    }

    public double getPercentage() {
        return getScore() / getMaxScore();
    }
}
