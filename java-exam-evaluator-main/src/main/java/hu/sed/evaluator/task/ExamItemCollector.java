package hu.sed.evaluator.task;

import hu.sed.evaluator.ReflectionUtils;
import hu.sed.evaluator.annotation.syntax.TypeCheck;
import hu.sed.evaluator.item.container.ItemContainer;
import hu.sed.evaluator.item.container.ListItemContainer;
import hu.sed.evaluator.item.Item;
import hu.sed.evaluator.item.ItemFactory;
import hu.sed.evaluator.item.semantic.TestItem;
import hu.sed.evaluator.item.syntax.FieldItem;
import hu.sed.evaluator.item.syntax.MethodItem;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class ExamItemCollector implements Task {

    ItemFactory itemFactory = new ItemFactory(); // TODO dependency injection

    @Override
    public void execute(TaskArgument argument) {
        List<Class<?>> examClasses = ReflectionUtils.getClassesOfPackage(argument.getExamPackage());

        ListItemContainer root = ListItemContainer.builder()
                .containerName("root")
                .items(examClasses.stream().map(this::getExamItem)
                        .filter(item -> item instanceof ListItemContainer containerItem && !containerItem.getItems().isEmpty())
                        .toList())
                .build();
        System.out.println(root); // TODO remove syso
    }

    private Item getExamItem(Class<?> clazz) {
        Optional<TypeCheck> annotation = ReflectionUtils.getAnnotation(TypeCheck.class, clazz);

        boolean includeUnannotatedFields = false, includeUnannotatedMethods = false;
        ItemContainer item;
        if (annotation.isEmpty() || ReflectionUtils.skipped(clazz)) {
            item = ListItemContainer.builder()
                    .containerName(clazz.getCanonicalName())
                    .build();
        } else {
            TypeCheck typeCheck = annotation.get();
            item = itemFactory.createItem(typeCheck, clazz);
            includeUnannotatedFields = typeCheck.checkFields();
            includeUnannotatedMethods = typeCheck.checkMethods();
        }

        // add fields
        List<Item> subItems = new ArrayList<>(getFieldItems(clazz.getDeclaredFields()));
        if (includeUnannotatedFields) {
            // TODO
        }

        // add constructors
        subItems.addAll(
                getConstructorItems(clazz.getConstructors())
        );
        if (includeUnannotatedMethods) {
            // TODO
        }

        // add methods
        subItems.addAll(
                getMethodItems(clazz.getDeclaredMethods())
        );
        if (includeUnannotatedMethods) {
            // TODO
        }

        // add custom tests
        subItems.addAll(
                getCustomTests(clazz)
        );

        // todo implement subclass..
        item.setItems(subItems);

        return item;
    }

    private List<TestItem> getCustomTests(Class<?> clazz) {
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

    private List<FieldItem> getFieldItems(Field[] fields) {
        return Arrays.stream(fields)
                .filter(ReflectionUtils::hasSyntaxCheckAnnotation)
                .filter(ReflectionUtils::notSkipped)
                .map(this::getFieldItems)
                .flatMap(Collection::stream)
                .toList();
    }

    private List<FieldItem> getFieldItems(Field field) {
        return ReflectionUtils.getAnnotations(field)
                .stream()
                .map(annotation -> itemFactory.createItem(annotation, field))
                .toList();
    }

    private List<MethodItem> getMethodItems(Method[] methods) {
        return Arrays.stream(methods)
                .filter(ReflectionUtils::hasSyntaxCheckAnnotation)
                .filter(ReflectionUtils::notSkipped)
                .map(this::getMethodItems)
                .flatMap(Collection::stream)
                .toList();
    }

    private List<MethodItem> getMethodItems(Method method) {
        return ReflectionUtils.getAnnotations(method)
                .stream()
                .map(annotation -> itemFactory.createItem(annotation, method))
                .toList();
    }

    private List<MethodItem> getConstructorItems(Constructor<?>[] constructors) {
        return Arrays.stream(constructors)
                .filter(ReflectionUtils::hasSyntaxCheckAnnotation)
                .filter(ReflectionUtils::notSkipped)
                .map(this::getConstructorItems)
                .flatMap(Collection::stream)
                .toList();
    }

    private List<MethodItem> getConstructorItems(Constructor<?> constructors) {
        return ReflectionUtils.getAnnotations(constructors)
                .stream()
                .map(annotation -> itemFactory.createItem(annotation, constructors))
                .toList();
    }
}
