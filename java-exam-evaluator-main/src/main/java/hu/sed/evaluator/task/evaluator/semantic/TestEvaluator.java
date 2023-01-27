package hu.sed.evaluator.task.evaluator.semantic;

import com.google.inject.Singleton;
import hu.sed.evaluator.annotation.semantic.CustomTestContants;
import hu.sed.evaluator.item.semantic.TestItem;
import hu.sed.evaluator.task.evaluator.Evaluator;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Slf4j
@Singleton
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TestEvaluator implements Evaluator<TestItem, ScoredSemanticItem> {

    @Override
    public ScoredSemanticItem evaluate(TestItem item) {
        log.debug("Evaluate item: {}", item.identifier());
        if (!StringUtils.isBlank(item.getDescription())) {
            log.info("Description of test: {}", item.getDescription());
        }
        ScoredSemanticItem scoredItem = ScoredSemanticItem.builder()
                .item(item)
                .build();

        Class<?> testClass;
        try {
            testClass = Class.forName(item.getTestClass());
        } catch (ClassNotFoundException e) {
            log.error("Cannot evaluate tests, testClass is not found: {}", item.getTestClass());
            scoredItem.unsuccessfulCheck("TEST_CLASS_NOT_FOUND");
            return scoredItem;
        }

        Optional<Object> testObjectOpt = getTestObject(testClass);
        if (testObjectOpt.isEmpty()) {
            return scoredItem;
        }

        Object testObject = testObjectOpt.get();
        List<Method> testMethods = collectTestMethods(scoredItem, item, testClass);

        for (Method testMethod : testMethods) {
            boolean result = executeTestMethod(testObject, testMethod);
            String name = testMethod.getName();
            log.info("Test result {}. {}.{}", (result ? "successful" : "unsuccessful"),
                    testObject.getClass().getName(), name);
            scoredItem.addCheck(name, result);
        }

        return scoredItem;
    }


    private Optional<Object> getTestObject(Class<?> testClass) {
        Optional<Object> testObject = Optional.empty();
        Optional<Method> setupMethod = Optional.empty();
        try {
            Optional<Constructor<?>> defaultConstructor = Arrays.stream(testClass.getDeclaredConstructors())
                    .filter(constructor -> constructor.getGenericParameterTypes().length == 0)
                    .findFirst();
            if (defaultConstructor.isPresent()) {
                testObject = Optional.of(defaultConstructor.get().newInstance());
            } else {
                log.error("There is no default constructor for testClass: {}", testClass);
            }

            setupMethod = Arrays.stream(testClass.getDeclaredMethods())
                    .filter(method -> "setup".equals(method.getName()))
                    .filter(method -> method.getGenericParameterTypes().length == 0)
                    .filter(method -> Modifier.isPublic(method.getModifiers()))
                    .findFirst();
        } catch (IllegalAccessException | InvocationTargetException | InstantiationException e) {
            log.error("Cannot evaluate tests, testClass cannot be instantiated or constructor cannot be invoked. " +
                    "Add default constructor or change it's visibility {}", testClass);
        }
        if (setupMethod.isPresent()) {
            try {
                setupMethod.get().invoke(testObject);
            } catch (IllegalAccessException | InvocationTargetException e) {
                log.error("Failed to setup test object.", e);
                testObject = Optional.empty();
            }
        }

        return testObject;
    }

    private List<Method> collectTestMethods(ScoredSemanticItem scoredSemanticItem, TestItem item, Class<?> testClass) {
        if (item.getTestMethods().length == 0) {
            return Collections.emptyList();
        }

        List<Method> testMethods = new ArrayList<>();
        List<String> testMethodNames = Arrays.asList(item.getTestMethods());
        Stream<Method> methodStream = Arrays.stream(testClass.getDeclaredMethods())
                .filter(method -> Modifier.isPublic(method.getModifiers()))
                .filter(method -> method.getGenericParameterTypes().length == 0);
        if (testMethodNames.size() == 1 && CustomTestContants.ALL_TEST.equals(testMethodNames.get(0))) {
            testMethods.addAll(methodStream.toList());
        } else {
            List<Method> methods = methodStream.toList();
            for (String testMethodName : testMethodNames) {
                Optional<Method> realMethod = methods.stream()
                        .filter(method -> method.getName().equals(testMethodName))
                        .findFirst();
                if (realMethod.isPresent()) {
                    testMethods.add(realMethod.get());
                } else {
                    scoredSemanticItem.addCheck(testMethodName, false);
                }
            }

        }
        return testMethods;
    }

    private boolean executeTestMethod(Object testObject, Method testMethod) {
        try {
            testMethod.invoke(testObject);
            return true;
        } catch (IllegalAccessException | InvocationTargetException e) {
            log.error("Cannot execute test method: {}.{}", testObject.getClass().getName(), testMethod.getName(), e);
        } catch (Exception e) {
            log.info("Test exception: ", e);
        }
        return false;
    }
}
