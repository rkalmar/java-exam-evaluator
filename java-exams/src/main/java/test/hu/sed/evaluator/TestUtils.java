package test.hu.sed.evaluator;

import javassist.util.proxy.MethodHandler;
import javassist.util.proxy.ProxyFactory;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import test.hu.sed.evaluator.exception.AssertionException;

import java.lang.reflect.Modifier;
import java.util.Arrays;

@Slf4j
@UtilityClass
public class TestUtils {
    public static void checkNoDefaultConstructor(Class<?> clazz) {
        boolean hasDefaultConstructor = Arrays.stream(clazz.getDeclaredConstructors())
                .anyMatch(constructor -> constructor.getGenericParameterTypes().length == 0);
        if (hasDefaultConstructor) {
            throw new AssertionException("There is default constructor in class: " + clazz.getSimpleName());
        }
    }

    /*
     * Assumes abstractClass has default constructor
     */
    public static <R> R createInstanceFromAbstractClass(Class<? extends R> abstractClass) {
        return createInstanceFromAbstractClass(abstractClass, defaultMethodHandler(), new Class<?>[0], new Object[0]);
    }

    /*
     * Pass constructor paramTypes and arguments by setting paramTypes and args arrays properly
     */
    public static <R> R createInstanceFromAbstractClass(Class<? extends R> abstractClass, Class<?>[] paramTypes, Object[] args) {
        return createInstanceFromAbstractClass(abstractClass, defaultMethodHandler(), paramTypes, args);
    }

    @SneakyThrows
    public static <R> R createInstanceFromAbstractClass(Class<? extends R> abstractClass, MethodHandler methodHandler, Class<?>[] paramTypes, Object[] args) {
        if (!Modifier.isAbstract(abstractClass.getModifiers())) {
            throw new IllegalArgumentException(abstractClass.getSimpleName() + " is not an abstract class.");
        }

        ProxyFactory proxyFactory = new ProxyFactory();
        proxyFactory.setSuperclass(abstractClass);
        proxyFactory.setFilter(method -> Modifier.isAbstract(method.getModifiers()));
        //noinspection unchecked
        return (R) proxyFactory.create(paramTypes, args, methodHandler);
    }

    private static MethodHandler defaultMethodHandler() {
        return (self, method, proceed, args) -> {
            log.info("Empty method handler of method: {}.{}", method.getDeclaringClass().getSimpleName(), method.getName());
            return null;
        };
    }
}
