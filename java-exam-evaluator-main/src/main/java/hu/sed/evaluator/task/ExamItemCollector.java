package hu.sed.evaluator.task;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import hu.sed.evaluator.annotation.syntax.TypeCheck;
import hu.sed.evaluator.item.Item;
import hu.sed.evaluator.item.ItemFactory;
import hu.sed.evaluator.item.ScorableItem;
import hu.sed.evaluator.item.container.ItemContainer;
import hu.sed.evaluator.item.container.ListItemContainer;
import hu.sed.evaluator.item.container.RootItem;
import hu.sed.evaluator.item.syntax.TypeItem;
import hu.sed.evaluator.task.argument.TaskArgument;
import hu.sed.evaluator.task.collector.ConstructorItemCollector;
import hu.sed.evaluator.task.collector.CustomTestItemCollector;
import hu.sed.evaluator.task.collector.FieldItemCollector;
import hu.sed.evaluator.task.collector.MethodItemCollector;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Singleton
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor(onConstructor = @__(@Inject))
class ExamItemCollector implements Task<RootItem> {

    ItemFactory itemFactory;

    FieldItemCollector fieldItemCollector;

    MethodItemCollector methodItemCollector;

    ConstructorItemCollector constructorItemCollector;

    CustomTestItemCollector customTestItemCollector;

    TaskArgument argument;

    @SneakyThrows
    @Override
    public RootItem execute() {
        log.info("Executing item collector for package: {}", argument.getExamPackage());
        List<? extends Class<?>> examClasses = ReflectionUtils.getClassesOfPackage(argument.getExamPackage());

        List<ItemContainer> items = examClasses.stream()
                .map(this::getExamItem)
                .filter(item -> item instanceof ScorableItem || !item.isEmpty())
                .toList();

        return RootItem.builder()
                .createdBy(System.getProperty("user.name"))
                .creationTime(LocalDateTime.now())
                .items(items)
                .containerName(argument.getExamPackage())
                .build();
    }

    private ItemContainer getExamItem(Class<?> clazz) {
        Optional<TypeCheck> annotation = ReflectionUtils.getAnnotation(TypeCheck.class, clazz);

        ItemContainer item;
        List<Item> subItems = new ArrayList<>();

        if (annotation.isEmpty() || ReflectionUtils.skipped(clazz)) {
            item = ListItemContainer.builder()
                    .containerName(clazz.getCanonicalName())
                    .build();
        } else {
            TypeCheck typeCheck = annotation.get();
            TypeItem typeItem = itemFactory.createItem(typeCheck, clazz);
            boolean needUnannotatedFields = typeCheck.checkFields();
            boolean needUnannotatedMethods = typeCheck.checkMethods();

            List<ScorableItem> unannotatedItems = new ArrayList<>();
            if (needUnannotatedFields) {
                unannotatedItems.addAll(fieldItemCollector.collectUnannotatedItems(clazz));
            }
            if (needUnannotatedMethods) {
                unannotatedItems.addAll(methodItemCollector.collectUnannotatedItems(clazz));
                unannotatedItems.addAll(constructorItemCollector.collectUnannotatedItems(clazz));
            }

            CalculationUtils.DistributedScore distributedScore =
                    CalculationUtils.distributeScore(typeCheck.score(), unannotatedItems.size() + 1);

            if (!distributedScore.isProperlyDistributed()) {
                log.warn("Score {}, cannot be distributed properly among {} items. Consider to change score of typeItem: {}",
                        typeCheck.score(), unannotatedItems.size() + 1, typeItem.identifier());
            }

            typeItem.setScore(distributedScore.mainScore());
            for (ScorableItem unannotatedItem : unannotatedItems) {
                unannotatedItem.setScore(distributedScore.subScore());
            }
            subItems.addAll(unannotatedItems);
            item = typeItem;
        }

        // add fields
        subItems.addAll(
                fieldItemCollector.collectItems(clazz)
        );

        // add constructors
        subItems.addAll(
                constructorItemCollector.collectItems(clazz)
        );

        // add methods
        subItems.addAll(
                methodItemCollector.collectItems(clazz)
        );

        // add custom tests
        subItems.addAll(
                customTestItemCollector.collectItems(clazz)
        );

        // add inner classes
        subItems.addAll(Arrays.stream(clazz.getDeclaredClasses())
                .map(this::getExamItem)
                .filter(innerClassItem -> !innerClassItem.isEmpty())
                .toList());

        subItems.sort(Comparator.comparing(o -> o.getClass().getName()));

        item.setItems(subItems);

        return item;
    }


}
