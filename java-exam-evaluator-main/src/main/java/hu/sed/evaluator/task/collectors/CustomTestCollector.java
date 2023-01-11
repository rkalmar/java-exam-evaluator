package hu.sed.evaluator.task.collectors;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import hu.sed.evaluator.ReflectionUtils;
import hu.sed.evaluator.item.ItemFactory;
import hu.sed.evaluator.item.semantic.TestItem;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Singleton
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public final class CustomTestCollector {

    @Inject
    ItemFactory itemFactory;

    public List<TestItem> collectItems(Class<?> clazz) {
        List<TestItem> testItems = new ArrayList<>(
                ReflectionUtils.getAnnotations(clazz).stream()
                        .map(itemFactory::createTestItem).toList()
        );

        testItems.addAll(ReflectionUtils.getClassMembers(clazz).stream()
                .filter(ReflectionUtils::hasSemanticTestAnnotation)
                .filter(ReflectionUtils::notSkipped)
                .map(ReflectionUtils::getAnnotations)
                .flatMap(Collection::stream)
                .map(itemFactory::createTestItem)
                .toList());

        return testItems;
    }
}
