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
        Optional<TypeCheck> typeCheck;

        if (annotation.isEmpty() || ReflectionUtils.skipped(clazz)) {
            item = ListItemContainer.builder()
                    .containerName(clazz.getCanonicalName())
                    .build();
            typeCheck = Optional.empty();
        } else {
            TypeCheck typeCheckAnnotation = annotation.get();
            item = itemFactory.createItem(typeCheckAnnotation, clazz);
            typeCheck = Optional.of(typeCheckAnnotation);
        }

        boolean needUnannotatedFields = typeCheck.isPresent() && typeCheck.get().checkFields();
        boolean needUnannotatedMethods = typeCheck.isPresent() && typeCheck.get().checkMethods();
        int scorePerField = typeCheck.map(TypeCheck::scorePerField).orElse(-1);
        int scorePerMethod = typeCheck.map(TypeCheck::scorePerMethod).orElse(-1);

        // add fields
        List<Item> subItems = new ArrayList<>(
                fieldItemCollector.collectItems(clazz, needUnannotatedFields, scorePerField)
        );

        // add constructors
        subItems.addAll(
                constructorItemCollector.collectItems(clazz, needUnannotatedMethods, scorePerMethod)
        );

        // add methods
        subItems.addAll(
                methodItemCollector.collectItems(clazz, needUnannotatedMethods, scorePerMethod)
        );

        // add custom tests
        subItems.addAll(
                customTestItemCollector.collectItems(clazz)
        );

        subItems.addAll(Arrays.stream(clazz.getDeclaredClasses())
                .map(this::getExamItem)
                .filter(innerClassItem -> !innerClassItem.isEmpty())
                .toList());

        item.setItems(subItems);

        return item;
    }


}
