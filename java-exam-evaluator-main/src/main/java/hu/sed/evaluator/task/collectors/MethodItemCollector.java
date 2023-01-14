package hu.sed.evaluator.task.collectors;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import hu.sed.evaluator.ReflectionUtils;
import hu.sed.evaluator.annotation.syntax.DefaultMethodCheck;
import hu.sed.evaluator.item.ItemFactory;
import hu.sed.evaluator.item.syntax.MethodItem;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.lang.reflect.Method;
import java.util.List;
import java.util.function.Function;

@Singleton
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public final class MethodItemCollector extends ItemCollector<MethodItem, Method> {

    ItemFactory itemFactory;

    @Override
    protected Function<Method, MethodItem> defaultItemCreatorFunc(int score) {
        return method -> itemFactory.createItem(DefaultMethodCheck.defaultCheck(score), method);
    }

    @Override
    protected Function<Method, List<MethodItem>> itemCreatorFunc() {
        return method -> ReflectionUtils.getAnnotations(method)
                .stream()
                .map(annotation -> itemFactory.createItem(annotation, method))
                .toList();
    }

    @Override
    protected Method[] getElements(Class<?> clazz) {
        return clazz.getDeclaredMethods();
    }
}
