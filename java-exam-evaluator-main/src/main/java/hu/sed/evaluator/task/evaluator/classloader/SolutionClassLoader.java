package hu.sed.evaluator.task.evaluator.classloader;

import java.net.URL;
import java.net.URLClassLoader;

public class SolutionClassLoader extends URLClassLoader {

    public SolutionClassLoader(URL solutionsUrl) {
        super(new URL[]{solutionsUrl});
    }

    public boolean isClassLoaded(String className) {
        return this.findLoadedClass(className) != null;
    }

    @Override
    public Class<?> findClass(String name) throws ClassNotFoundException {
        return super.findClass(name);
    }

    @Override
    protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        Class<?> loadedClass = findLoadedClass(name);
        if (loadedClass == null) {
            try {
                loadedClass = findClass(name);
            } catch (ClassNotFoundException e) {
                loadedClass = super.loadClass(name, resolve);
            }
        }

        if (resolve) {
            resolveClass(loadedClass);
        }
        return loadedClass;
    }

}
