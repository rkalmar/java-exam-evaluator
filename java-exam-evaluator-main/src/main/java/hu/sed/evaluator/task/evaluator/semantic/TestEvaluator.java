package hu.sed.evaluator.task.evaluator.semantic;

import com.google.inject.Singleton;
import hu.sed.evaluator.annotation.semantic.CustomTestContants;
import hu.sed.evaluator.annotation.test.BeforeEach;
import hu.sed.evaluator.annotation.test.Setup;
import hu.sed.evaluator.annotation.test.ExamTest;
import hu.sed.evaluator.item.semantic.TestItem;
import hu.sed.evaluator.task.evaluator.Evaluator;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.lang.annotation.Annotation;
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
        log.info("Evaluate item: {}", item.identifier());
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

        if (item.getTestMethods().length > 0 &&
                testMethods.isEmpty()) {
            log.error("Failed to execute tests, failed to load configured method(s)");
        }
        Optional<Method> beforeEachMethod = getMethodByAnnotation(testClass, BeforeEach.class);
        for (Method testMethod : testMethods) {
            boolean result = executeTestMethod(testObject, testMethod, beforeEachMethod);
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

            setupMethod = getMethodByAnnotation(testClass, Setup.class);
        } catch (IllegalAccessException | InvocationTargetException | InstantiationException e) {
            log.error("Cannot evaluate tests, testClass cannot be instantiated or constructor cannot be invoked. " +
                    "Add default constructor or change it's visibility {}", testClass);
        }
        if (setupMethod.isPresent() && testObject.isPresent()) {
            try {
                setupMethod.get().invoke(testObject.get());
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
                .filter(method -> method.getGenericParameterTypes().length == 0)
                .filter(method -> method.isAnnotationPresent(ExamTest.class));
        if (testMethodNames.size() == 1 && CustomTestContants.ALL_TEST.equals(testMethodNames.get(0))) {
            List<Method> methods = methodStream.toList();
            item.setTestMethods(methods.stream().map(Method::getName).toArray(String[]::new));
            testMethods.addAll(methods);
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

    private Optional<Method> getMethodByAnnotation(Class<?> testClass, Class<? extends Annotation> annotation) {
        return Arrays.stream(testClass.getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(annotation))
                .filter(method -> method.getGenericParameterTypes().length == 0)
                .filter(method -> Modifier.isPublic(method.getModifiers()))
                .findFirst();
    }

    private boolean executeTestMethod(Object testObject, Method testMethod, Optional<Method> beforeEachMethodOpt) {
        if (beforeEachMethodOpt.isPresent()) {
            try {
                beforeEachMethodOpt.get().invoke(testObject);
                return executeTestMethod(testObject, testMethod);
            } catch (IllegalAccessException | InvocationTargetException e) {
                log.error("Cannot execute beforeEach method: {}.{}", testObject.getClass().getName(), testMethod.getName(), e);
            } catch (Exception e) {
                log.info("beforeEach exception: ", e);
            }
            return false;
        }

        return executeTestMethod(testObject, testMethod);
    }

    private boolean executeTestMethod(Object testObject, Method testMethod) {
        try {
            testMethod.invoke(testObject);
            return true;
        } catch (IllegalAccessException e) {
            log.error("Cannot execute test method: {}.{}", testObject.getClass().getName(), testMethod.getName(), e);
        } catch (InvocationTargetException e) {
            if (e.getCause() instanceof AssertionError assertionError) {
                log.error("Test exception: {}", assertionError.getMessage());
            } else {
                log.error("Cannot execute test method: {}.{}", testObject.getClass().getName(), testMethod.getName(), e);
            }
        } catch (Exception e) {
            log.info("Test exception: ", e);
        }
        return false;
    }
}
