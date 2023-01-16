package hu.sed.evaluator.task.evaluators;

import hu.sed.evaluator.item.Item;

public interface Evaluator<T extends Item, R extends ScoredItem> {

    R evaluate(T item);
}
