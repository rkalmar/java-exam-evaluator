package hu.sed.evaluator.task.evaluators;

import hu.sed.evaluator.item.Item;

public interface Evaluator<T extends Item> {

    ScoredItem evaluate(T item);
}
