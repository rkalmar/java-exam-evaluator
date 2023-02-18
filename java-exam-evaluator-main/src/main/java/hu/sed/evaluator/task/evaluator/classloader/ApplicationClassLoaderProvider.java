package hu.sed.evaluator.task.evaluator.classloader;

public final class ApplicationClassLoaderProvider implements ClassLoaderProvider {

    @Override
    public ClassLoader get() {
        return Thread.currentThread().getContextClassLoader();
    }
}
