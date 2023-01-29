package test.hu.sed.evaluator.exam;

import org.junit.jupiter.api.BeforeEach;

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

public abstract class ExamTestEvaluatorBase {

    private final Class<?> testClass;
    private Object testObject;
    private final Map<String, Method> testMethods;
    private Optional<Method> beforeEachMethod;

    public ExamTestEvaluatorBase(Class<?> testClass) {
        this.testClass = testClass;
        testMethods = new HashMap<>();
        beforeEachMethod = Optional.empty();
    }

    @BeforeEach
    public void setup() {
        constructObject();
        setupObject();
        findPotentialTestMethods();
        setupBeforeEachMethod();
    }

    protected void callTestMethod(String testMethod) {
        try {
            if (beforeEachMethod.isPresent()) {
                beforeEachMethod.get().invoke(testObject);
            }
            System.out.println("Calling test method " + testMethod);
            testMethods.get(testMethod).invoke(testObject);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException("Failed to run test: " + testMethod);
        }
    }

    protected void callAllTestMethod() {
        testMethods.keySet().forEach(this::callTestMethod);
    }

    private void findPotentialTestMethods() {
        Map<String, List<Method>> collectedMethods = Arrays.stream(testClass.getDeclaredMethods())
                .filter(method -> Modifier.isPublic(method.getModifiers()))
                .filter(method -> method.getGenericParameterTypes().length == 0)
                .filter(method -> !Arrays.asList("setup", "beforeEach").contains(method.getName()))
                .peek(method -> System.out.println("Test method found: " + method.getName()))
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
                .filter(method -> "setup".equals(method.getName()))
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
            System.out.println("No setup method found..");
        }
    }

    private void setupBeforeEachMethod() {
        this.beforeEachMethod = Arrays.stream(testClass.getDeclaredMethods())
                .filter(method -> "beforeEach".equals(method.getName()))
                .filter(method -> method.getGenericParameterTypes().length == 0)
                .filter(method -> Modifier.isPublic(method.getModifiers()))
                .findFirst();
        if (beforeEachMethod.isEmpty()) {
            System.out.println("No beforeEach method found..");
        }
    }
}
