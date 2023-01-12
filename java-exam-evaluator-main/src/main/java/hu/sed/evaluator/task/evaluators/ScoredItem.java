package hu.sed.evaluator.task.evaluators;

import hu.sed.evaluator.item.BaseItem;
import hu.sed.evaluator.item.Item;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.Singular;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ScoredItem {

    BaseItem item;

    @Builder.Default
    List<Item> children = new ArrayList<>();

    @Singular
    List<CheckedElement> correctElements;

    @Singular
    List<CheckedElement> incorrectElements;

    public final double getScore() {
        int checkedItemCount = correctElements.size() + incorrectElements.size();
        return ((double) item.getScore() / checkedItemCount) * correctElements.size();
    }
}
