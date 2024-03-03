package test.hu.sed.evaluator.context;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.lang.reflect.Method;

@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class TestEvaluationContext implements AutoCloseable {

    private static ThreadLocal<TestEvaluationContext> CONTEXT = new ThreadLocal<>();

    final Class<?> testClass;

    final Method testMethod;

    final Object testObject;

    boolean beforeMethodSuccessful;

    boolean testMethodSuccessful;

    public static TestEvaluationContext createContext(Class<?> testClass, Method testMethod, Object testObject) {
        TestEvaluationContext testEvaluationContext = new TestEvaluationContext(testClass, testMethod, testObject);
        CONTEXT.set(testEvaluationContext);
        return testEvaluationContext;
    }

    public static Class<?> getTestClass() {
        validateContext();
        return CONTEXT.get().testClass;
    }

    public static Method getTestMethod() {
        validateContext();
        return CONTEXT.get().testMethod;
    }


    public static Object getTestObject() {
        validateContext();
        return CONTEXT.get().testObject;
    }

    public static boolean isBeforeMethodSuccessful() {
        validateContext();
        return CONTEXT.get().beforeMethodSuccessful;
    }

    public static boolean isTestMethodSuccessful() {
        validateContext();
        return CONTEXT.get().testMethodSuccessful;
    }

    public void setBeforeMethodResult(boolean result) {
        CONTEXT.get().beforeMethodSuccessful = result;
    }

    public void setTestMethodResult(boolean result) {
        CONTEXT.get().testMethodSuccessful = result;
    }

    private static void validateContext() {
        if (CONTEXT.get() == null) {
            throw new IllegalStateException("Try to reach context outside of test evaluation scope.");
        }
    }

    @Override
    public void close() {
        CONTEXT.remove();
    }
}
