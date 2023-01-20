package hu.sed.evaluator.task.collector;

import com.google.common.collect.Lists;
import hu.sed.evaluator.task.ReflectionUtils;
import hu.sed.evaluator.item.Item;

import java.lang.reflect.AccessibleObject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

public abstract class ItemCollector<T extends Item, R extends AccessibleObject> {

    public List<T> collectItems(Class<?> clazz, boolean needUnannotatedItems, int scorePerUnannotatedItem) {
        R[] elements = getElements(clazz);
        List<T> subItems = new ArrayList<>(this.getAnnotatedItems(elements));
        if (needUnannotatedItems) {
            subItems.addAll(
                    this.getUnannotatedItems(elements, scorePerUnannotatedItem)
            );
        }
        return subItems;
    }

    private final List<T> getUnannotatedItems(R[] elements, int scorePerItem) {
        return this.getItems(elements, Predicate.not(ReflectionUtils::hasSyntaxCheckAnnotation),
                field -> Lists.newArrayList(
                        defaultItemCreatorFunc(scorePerItem).apply(field)
                )
        );
    }

    private final List<T> getAnnotatedItems(R[] elements) {
        return this.getItems(elements, ReflectionUtils::hasSyntaxCheckAnnotation,
                field -> itemCreatorFunc().apply(field));
    }

    private List<T> getItems(R[] fields, Predicate<R> filter, Function<R, List<T>> itemCreatorFunc) {
        return Arrays.stream(fields)
                .filter(filter)
                .filter(ReflectionUtils::notSkipped)
                .map(itemCreatorFunc)
                .flatMap(Collection::stream)
                .toList();
    }

    protected abstract Function<R, T> defaultItemCreatorFunc(int score);

    protected abstract Function<R, List<T>> itemCreatorFunc();

    protected abstract R[] getElements(Class<?> clazz);

}
