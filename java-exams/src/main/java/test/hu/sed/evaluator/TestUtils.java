package test.hu.sed.evaluator;

import test.hu.sed.evaluator.exception.CheckFailureException;

import java.util.Arrays;

public final class TestUtils {
    private TestUtils() {
        throw new UnsupportedOperationException();
    }

    public static void checkNoDefaultConstructor(Class<?> clazz) {
        boolean hasDefaultConstructor = Arrays.stream(clazz.getDeclaredConstructors())
                .anyMatch(constructor -> constructor.getGenericParameterTypes().length == 0);
        if (hasDefaultConstructor) {
            throw new CheckFailureException("There is default constructor in class: " + clazz.getSimpleName());
        }
    }
}
