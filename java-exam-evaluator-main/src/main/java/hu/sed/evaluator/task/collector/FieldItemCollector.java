package hu.sed.evaluator.task.collector;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import hu.sed.evaluator.annotation.syntax.DefaultFieldCheck;
import hu.sed.evaluator.item.ItemFactory;
import hu.sed.evaluator.item.syntax.FieldItem;
import hu.sed.evaluator.task.ReflectionUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.lang.reflect.Field;
import java.util.List;
import java.util.function.Function;

@Singleton
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public final class FieldItemCollector extends ItemCollector<FieldItem, Field> {

    ItemFactory itemFactory;

    @Override
    protected Function<Field, FieldItem> defaultItemCreatorFunc(int score) {
        return field -> itemFactory.createItem(DefaultFieldCheck.defaultCheck(score), field);
    }

    @Override
    protected Function<Field, List<FieldItem>> itemCreatorFunc() {
        return field -> ReflectionUtils.getAnnotations(field)
                .stream()
                .map(annotation -> itemFactory.createItem(annotation, field))
                .toList();
    }

    @Override
    protected Field[] getElements(Class<?> clazz) {
        return clazz.getDeclaredFields();
    }
}
