package test.hu.sed.evaluator.exam;

import hu.sed.evaluator.annotation.test.ExamTest;
import hu.sed.evaluator.annotation.test.Setup;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import test.hu.sed.evaluator.context.TestEvaluationContext;
import test.hu.sed.evaluator.exception.AssertionException;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.util.stream.Collectors.groupingBy;

@Slf4j
public abstract class ExamTestEvaluatorBase {

    private final Class<?> testClass;
    private Object testObject;
    private final Map<String, Method> testMethods;
    private Optional<Method> beforeEachMethod;
    private Optional<Method> afterEachMethod;

    public ExamTestEvaluatorBase(Class<?> testClass) {
        this.testClass = testClass;
        testMethods = new HashMap<>();
        beforeEachMethod = Optional.empty();
        afterEachMethod = Optional.empty();
    }

    @BeforeEach
    public void setup() {
        constructObject();
        setupObject();
        findPotentialTestMethods();
        setupBeforeEachMethod();
        setupAfterEachMethod();
    }

    @Test
    public void testAll() {
        this.callAllTestMethod();
    }

    protected void callTestMethod(String testMethodName) {
        Method method = testMethods.get(testMethodName);
        try (TestEvaluationContext testEvaluationContext = TestEvaluationContext.createContext(testClass, method, testObject)) {
            if (beforeEachMethod.isPresent()) {
                beforeEachMethod.get().invoke(testObject);
            }
            testEvaluationContext.setBeforeMethodResult(true);

            log.info("Calling test method {}", testMethodName);
            method.invoke(testObject);
            testEvaluationContext.setTestMethodResult(true);

            if (afterEachMethod.isPresent()) {
                afterEachMethod.get().invoke(testObject);
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Failed to run test: " + testMethodName);
        } catch (InvocationTargetException e) {
            if (e.getCause() instanceof AssertionError assertionError) {
                throw new AssertionException(assertionError);
            } else {
                throw new RuntimeException("Failed to run test: " + testMethodName);
            }
        }
    }

    protected void callAllTestMethod() {
        testMethods.keySet().forEach(this::callTestMethod);
    }

    private void findPotentialTestMethods() {
        Map<String, List<Method>> collectedMethods = Arrays.stream(testClass.getDeclaredMethods())
                .filter(method -> Modifier.isPublic(method.getModifiers()))
                .filter(method -> method.getGenericParameterTypes().length == 0)
                .filter(method -> method.isAnnotationPresent(ExamTest.class))
                .peek(method -> log.info("Test method found: {}", method.getName()))
                .collect(groupingBy(Method::getName));
        collectedMethods.forEach((methodName, testMethods) -> {
            if (!collectedMethods.get(methodName).isEmpty()) {
                this.testMethods.put(methodName, testMethods.get(0));
            }
        });
        if (testMethods.isEmpty()) {
            throw new RuntimeException("No test method found..");
        }
    }

    private void constructObject() {
        Constructor<?> defaultConstructor = Arrays.stream(testClass.getDeclaredConstructors())
                .filter(constructor -> constructor.getGenericParameterTypes().length == 0)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("TestClass cannot be instantiated. " +
                        "There is no default constructor. " + testClass.getSimpleName()));

        try {
            testObject = defaultConstructor.newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException("TestClass instantiation failure: ", e);
        }
    }

    private void setupObject() {
        Optional<Method> setupMethod = Arrays.stream(testClass.getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(Setup.class))
                .filter(method -> method.getGenericParameterTypes().length == 0)
                .filter(method -> Modifier.isPublic(method.getModifiers()))
                .findFirst();

        if (setupMethod.isPresent()) {
            try {
                setupMethod.get().invoke(testObject);
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        } else {
            log.info("No setup method found..");
        }
    }

    private void setupBeforeEachMethod() {
        this.beforeEachMethod = getMethod(hu.sed.evaluator.annotation.test.BeforeEach.class);
        if (beforeEachMethod.isEmpty()) {
            log.info("No beforeEach method found..");
        }
    }

    private void setupAfterEachMethod() {
        this.afterEachMethod = getMethod(hu.sed.evaluator.annotation.test.AfterEach.class);
        if (afterEachMethod.isEmpty()) {
            log.info("No afterEach method found..");
        }
    }

    private boolean executeMethod(Object testObject, Method method) {
        try {
            method.invoke(testObject);
            return true;
        } catch (IllegalAccessException e) {
            log.error("Cannot execute method: {}.{}", testObject.getClass().getName(), method.getName(), e);
        } catch (InvocationTargetException e) {
            Throwable cause = e.getCause();
            log.error("Failed to execute method: {}, {} - {}", method.getName(), cause.getClass().getSimpleName(), cause.getMessage());
        } catch (Throwable e) {
            log.info("Exception, while executing method: {} ", method.getName(), e);
        }
        return false;
    }

    private Optional<Method> getMethod(Class<? extends Annotation> annotation) {
        return Arrays.stream(testClass.getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(annotation))
                .filter(method -> method.getGenericParameterTypes().length == 0)
                .filter(method -> Modifier.isPublic(method.getModifiers()))
                .findFirst();
    }
}
