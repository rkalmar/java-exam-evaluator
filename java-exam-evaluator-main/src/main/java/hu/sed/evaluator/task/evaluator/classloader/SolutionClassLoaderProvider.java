package hu.sed.evaluator.task.evaluator.classloader;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;

import java.io.File;
import java.net.URL;

@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public final class SolutionClassLoaderProvider implements ClassLoaderProvider {

    String solutionClassPath;

    @Override
    public ClassLoader get() {
        return new SolutionClassLoader(toUrl(solutionClassPath));
    }

    @SneakyThrows
    private URL toUrl(String classPath) {
        return new File(classPath).toURI().toURL();
    }
}
