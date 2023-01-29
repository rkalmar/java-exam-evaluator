package test.hu.sed.evaluator;

import lombok.experimental.UtilityClass;
import test.hu.sed.evaluator.exception.AssertionException;

import java.util.Arrays;

@UtilityClass
public class TestUtils {
    public static void checkNoDefaultConstructor(Class<?> clazz) {
        boolean hasDefaultConstructor = Arrays.stream(clazz.getDeclaredConstructors())
                .anyMatch(constructor -> constructor.getGenericParameterTypes().length == 0);
        if (hasDefaultConstructor) {
            throw new AssertionException("There is default constructor in class: " + clazz.getSimpleName());
        }
    }
}
