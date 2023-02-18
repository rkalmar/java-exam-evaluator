package hu.sed.evaluator.task.evaluator;

import hu.sed.evaluator.item.Item;
import hu.sed.evaluator.task.ScoredItem;

public interface Evaluator<T extends Item, R extends ScoredItem> {

    R evaluate(T item);
}
