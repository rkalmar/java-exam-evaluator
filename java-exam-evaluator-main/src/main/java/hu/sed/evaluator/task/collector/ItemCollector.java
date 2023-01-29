package hu.sed.evaluator.task.collector;

import com.google.common.collect.Lists;
import hu.sed.evaluator.item.Item;
import hu.sed.evaluator.task.ReflectionUtils;

import java.lang.reflect.AccessibleObject;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

public abstract class ItemCollector<T extends Item, R extends AccessibleObject> {

    public List<T> collectItems(Class<?> clazz) {
        return this.getAnnotatedItems(getElements(clazz));
    }

    public List<T> collectUnannotatedItems(Class<?> clazz) {
        R[] elements = getElements(clazz);
        return this.getItems(elements, Predicate.not(ReflectionUtils::hasSyntaxCheckAnnotation),
                field -> Lists.newArrayList(defaultItemCreatorFunc().apply(field))
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

    protected abstract Function<R, T> defaultItemCreatorFunc();

    protected abstract Function<R, List<T>> itemCreatorFunc();

    protected abstract R[] getElements(Class<?> clazz);

}
