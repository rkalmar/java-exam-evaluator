package hu.sed.evaluator.task;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import hu.sed.evaluator.item.Item;
import hu.sed.evaluator.item.ScorableItem;
import hu.sed.evaluator.item.container.ItemContainer;
import hu.sed.evaluator.item.container.RootItem;
import hu.sed.evaluator.task.evaluators.EvaluatorItemVisitor;
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

        rootItem.getItems().forEach(
                item -> evaluateItem(scoredItems, item)
        );

        return Score.builder()
                .scoredItems(scoredItems)
                .build();
    }

    private void evaluateItem(List<ScoredItem<?>> scoredItems, Item item) {
        if (item instanceof ScorableItem) {
            ScoredItem<?> scoredItem = item.accept(evaluatorItemVisitor);
            scoredItems.add(scoredItem);
        }
        if (item instanceof ItemContainer container) {
            container.getItems().forEach(childItem -> evaluateItem(scoredItems, childItem));
        }
    }
}
