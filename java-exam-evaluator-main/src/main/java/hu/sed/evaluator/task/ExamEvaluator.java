package hu.sed.evaluator.task;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import hu.sed.evaluator.item.Item;
import hu.sed.evaluator.item.ScorableItem;
import hu.sed.evaluator.item.container.ItemContainer;
import hu.sed.evaluator.item.container.RootItem;
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
        log.debug("Execute evaluator");
        List<ScoredItem<?>> scoredItems = new ArrayList<>();

        rootItem.getItems()
                .forEach(item -> evaluateItem(scoredItems, item));

        if (log.isErrorEnabled()) { //TODO
            scoredItems.forEach(scoredItem ->
                    log.error("{} - {}/{}", scoredItem.identifier(), scoredItem.getScore(), (double) scoredItem.getMaxScore())
            );
        }

        Score score = Score.builder()
                .scoredItems(scoredItems)
                .build();
        log.error("Score/TotalScore - {}/{} ({}%)", score.getScore(), score.getMaxScore(), score.getPercentage());

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
