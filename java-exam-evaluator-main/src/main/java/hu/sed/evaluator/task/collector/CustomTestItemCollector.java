package hu.sed.evaluator.task.collector;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import hu.sed.evaluator.item.ItemFactory;
import hu.sed.evaluator.item.semantic.TestItem;
import hu.sed.evaluator.task.ReflectionUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Singleton
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public final class CustomTestItemCollector {

    ItemFactory itemFactory;

    public List<TestItem> collectItems(Class<?> clazz) {
        List<TestItem> testItems = new ArrayList<>(
                ReflectionUtils.getAnnotations(clazz).stream()
                        .map(customTest -> itemFactory.createTestItem(customTest, clazz)).toList()
        );

        testItems.addAll(ReflectionUtils.getClassMembers(clazz).stream()
                .filter(ReflectionUtils::hasSemanticTestAnnotation)
                .filter(ReflectionUtils::notSkipped)
                .map(ReflectionUtils::getAnnotations)
                .flatMap(Collection::stream)
                .map(customTest -> itemFactory.createTestItem(customTest, clazz))
                .toList());

        return testItems;
    }
}
