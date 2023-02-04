package hu.sed.evaluator.task.evaluator.classloader;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;

import java.util.Map;
import java.util.Objects;


@Slf4j
@UtilityClass
public class ClassLoaderUtils {

    public static void executeInClassLoaderContext(ClassLoader classLoader, Executable executable) {
        ClassLoader originalClassloader = Thread.currentThread().getContextClassLoader();
        Thread.currentThread().setContextClassLoader(classLoader);
        try {
            executable.execute();
        } finally {
            Thread.currentThread().setContextClassLoader(originalClassloader);
        }
    }

    @SneakyThrows
    public static Class<?> injectClassToClassLoader(Class<?> clazz, ClassLoader targetClassloader) {
        byte[] code = Objects.requireNonNull(clazz.getResourceAsStream(clazz.getSimpleName() + ".class"))
                .readAllBytes();
        TypeDescription.ForLoadedType typeDescription = new TypeDescription.ForLoadedType(clazz);
        Map<TypeDescription, byte[]> name = Map.of(typeDescription, code);

        Map<TypeDescription, Class<?>> load = ClassLoadingStrategy.Default.INJECTION.load(targetClassloader, name);
        return load.getOrDefault(typeDescription, clazz);
    }

    @FunctionalInterface
    public interface Executable {
        void execute();
    }
}
