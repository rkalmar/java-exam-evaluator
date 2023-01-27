package hu.sed.evaluator.task;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import hu.sed.evaluator.item.Item;
import hu.sed.evaluator.item.ScorableItem;
import hu.sed.evaluator.item.container.ItemContainer;
import hu.sed.evaluator.item.container.RootItem;
import hu.sed.evaluator.item.syntax.TypeItem;
import hu.sed.evaluator.task.evaluator.EvaluatorItemVisitor;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Singleton
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class ExamEvaluator implements Task<Score, RootItem> {

    EvaluatorItemVisitor evaluatorItemVisitor;

    @Override
    public Score execute(RootItem rootItem) {
        log.debug("Executing evaluator..");
        List<ScoredItem<?>> scoredItems = new ArrayList<>();

        rootItem.getItems()
                .forEach(item -> evaluateItem(scoredItems, item));

        Score score = Score.builder()
                .scoredItems(scoredItems)
                .build();
        if (log.isInfoEnabled()) {
            StringBuilder message = new StringBuilder();
            scoredItems.forEach(scoredItem -> {
                        if (scoredItem.getItem() instanceof TypeItem) {
                            message.append(System.lineSeparator());
                        }
                        message.append(String.format("\r %.2f/%.2f - %s %s %s",
                                scoredItem.getScore(), (double) scoredItem.getMaxScore(), scoredItem.identifier(),
                                scoredItem.getUnsuccessfulChecks().isEmpty() ? "" : "Failed checks: " + scoredItem.getUnsuccessfulChecks(), System.lineSeparator()));
                    }
            );
            log.info("Evaluation result: {} {} {}/{} ({}%)", message, System.lineSeparator(), score.getScore(), score.getMaxScore(),
                    String.format("%.0f", score.getPercentage() * 100));
        }

        // TODO print result to file
        // TODO ADD grade tresholds json, and calculate grade by score

        return score;
    }

    private void evaluateItem(List<ScoredItem<?>> scoredItems, Item item) {
        if (item instanceof ScorableItem) {
            ScoredItem<?> scoredItem = item.accept(evaluatorItemVisitor);
            scoredItems.add(scoredItem);
        }
        if (item instanceof ItemContainer) {
            ((ItemContainer) item).getItems().forEach(childItem -> evaluateItem(scoredItems, childItem));
        }
    }
}
