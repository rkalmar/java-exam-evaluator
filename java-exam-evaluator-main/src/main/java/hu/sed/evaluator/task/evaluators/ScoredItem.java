package hu.sed.evaluator.task.evaluators;

import hu.sed.evaluator.item.BaseItem;
import hu.sed.evaluator.item.Item;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ScoredItem {

    BaseItem item;

    @Builder.Default
    List<Item> children = new ArrayList<>();

    @Builder.Default
    Map<CheckedElement, Boolean> checkedElements = new HashMap<>();

    public final void successfulElement(CheckedElement checkedElement) {
        element(checkedElement, true);
    }

    public final void unsuccessfulElement(CheckedElement checkedElement) {
        element(checkedElement, false);
    }

    public final void element(CheckedElement checkedElement, boolean checkResult) {
        checkedElements.put(checkedElement, checkResult);
    }

    public final double getScore() {
        if (checkedElements.size() == 0) {
           return 0;
        }
        long successfulCount = checkedElements.values().stream().filter(value -> value).count();
        return ((double) item.getScore() / checkedElements.size()) * successfulCount;
    }
}
