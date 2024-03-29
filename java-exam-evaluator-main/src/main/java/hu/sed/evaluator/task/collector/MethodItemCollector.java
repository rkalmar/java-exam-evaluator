package hu.sed.evaluator.task.collector;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import hu.sed.evaluator.annotation.syntax.DefaultMethodCheck;
import hu.sed.evaluator.item.ItemFactory;
import hu.sed.evaluator.item.syntax.MethodItem;
import hu.sed.evaluator.task.ReflectionUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

@Singleton
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public final class MethodItemCollector extends ItemCollector<MethodItem, Method> {

    ItemFactory itemFactory;

    @Override
    protected Function<Method, MethodItem> defaultItemCreatorFunc() {
        return method -> itemFactory.createItem(DefaultMethodCheck.defaultCheck(), method);
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
        if (clazz.isEnum()) {
            return Arrays.stream(clazz.getDeclaredMethods())
                    .filter(method -> !method.getName().contains("$") &&
                            !Arrays.asList("valueOf", "values").contains(method.getName()))
                    .toArray(Method[]::new);
        }
        return clazz.getDeclaredMethods();
    }
}
