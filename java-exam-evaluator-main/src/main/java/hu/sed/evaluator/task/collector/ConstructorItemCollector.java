package hu.sed.evaluator.task.collector;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import hu.sed.evaluator.annotation.syntax.DefaultConstructorCheck;
import hu.sed.evaluator.item.ItemFactory;
import hu.sed.evaluator.item.syntax.ConstructorItem;
import hu.sed.evaluator.task.ReflectionUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.lang.reflect.Constructor;
import java.util.List;
import java.util.function.Function;

@Singleton
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public final class ConstructorItemCollector extends ItemCollector<ConstructorItem, Constructor<?>> {

    ItemFactory itemFactory;

    @Override
    protected Function<Constructor<?>, ConstructorItem> defaultItemCreatorFunc() {
        return constructor -> itemFactory.createItem(DefaultConstructorCheck.defaultCheck(), constructor);
    }

    @Override
    protected Function<Constructor<?>, List<ConstructorItem>> itemCreatorFunc() {
        return constructor -> ReflectionUtils.getAnnotations(constructor)
                .stream()
                .map(annotation -> itemFactory.createItem(annotation, constructor))
                .toList();
    }

    @Override
    protected Constructor<?>[] getElements(Class<?> clazz) {
        return clazz.getConstructors();
    }
}
