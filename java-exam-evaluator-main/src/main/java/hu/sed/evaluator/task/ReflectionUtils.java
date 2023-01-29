package hu.sed.evaluator.task;

import hu.sed.evaluator.annotation.semantic.CustomTest;
import hu.sed.evaluator.annotation.semantic.SemanticTest;
import hu.sed.evaluator.annotation.syntax.ConstructorCheck;
import hu.sed.evaluator.annotation.syntax.FieldCheck;
import hu.sed.evaluator.annotation.syntax.MethodCheck;
import hu.sed.evaluator.annotation.syntax.SkipCheck;
import hu.sed.evaluator.annotation.syntax.SyntaxCheck;
import hu.sed.evaluator.annotation.uml.SkipFromUml;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Slf4j
@UtilityClass
public class ReflectionUtils {

    public List<Class<?>> getClassesOfPackage(String packageName) {
        Reflections reflections = new Reflections(packageName, Scanners.SubTypes.filterResultsBy(s -> true));
        reflections.getSubTypesOf(Object.class);
        List<String> classNames = reflections.getAll(Scanners.SubTypes)
                .stream().filter(s -> s.startsWith(packageName)).toList();
        List<Class<?>> result = new ArrayList<>();
        for (String className : classNames) {
            try {
                result.add(Class.forName(className));
            } catch (ClassNotFoundException e) {
                log.error("Failed to load class {}", className);
            }
        }

        return result;
    }

    public boolean hasSyntaxCheckAnnotation(AnnotatedElement accessibleObject) {
        return Arrays.stream(accessibleObject.getDeclaredAnnotations())
                .anyMatch(annotation -> annotation.annotationType().isAnnotationPresent(SyntaxCheck.class));
    }

    public boolean hasSemanticTestAnnotation(AnnotatedElement accessibleObject) {
        return Arrays.stream(accessibleObject.getDeclaredAnnotations())
                .anyMatch(annotation -> annotation.annotationType().isAnnotationPresent(SemanticTest.class));
    }

    public boolean notSkipped(AnnotatedElement annotatedElement) {
        return !annotatedElement.isAnnotationPresent(SkipCheck.class);
    }

    public boolean skipped(Class<?> clazz) {
        return clazz.isAnnotationPresent(SkipCheck.class);
    }

    public boolean notUmlSkipped(AnnotatedElement annotatedElement) {
        return !annotatedElement.isAnnotationPresent(SkipFromUml.class);
    }

    public <T extends Annotation> Optional<T> getAnnotation(Class<T> annotationClazz, Class<?> clazz) {
        return clazz.isAnnotationPresent(annotationClazz) ? Optional.of(clazz.getAnnotation(annotationClazz)) : Optional.empty();
    }

    public List<FieldCheck> getAnnotations(Field field) {
        return getAnnotations(FieldCheck.class, field);
    }

    public List<MethodCheck> getAnnotations(Method method) {
        return getAnnotations(MethodCheck.class, method);
    }

    public List<ConstructorCheck> getAnnotations(Constructor<?> constructor) {
        return getAnnotations(ConstructorCheck.class, constructor);
    }

    public <T extends Annotation> List<T> getAnnotations(Class<T> annotationClazz, AnnotatedElement annotatedElement) {
        return Arrays.asList(annotatedElement.getAnnotationsByType(annotationClazz));
    }

    public List<CustomTest> getAnnotations(Class<?> clazz) {
        return List.of(clazz.getDeclaredAnnotationsByType(CustomTest.class));
    }

    public List<CustomTest> getAnnotations(AnnotatedElement annotatedElement) {
        return List.of(annotatedElement.getDeclaredAnnotationsByType(CustomTest.class));
    }

    public List<AnnotatedElement> getClassMembers(Class<?> clazz) {
        List<AnnotatedElement> members = new ArrayList<>(List.of(clazz.getDeclaredFields()));
        members.addAll(List.of(clazz.getConstructors()));
        members.addAll(List.of(clazz.getDeclaredMethods()));
        return members;
    }

    public Field getFieldByName(String containerClass, String fieldName) throws ClassNotFoundException, NoSuchFieldException {
        return Class.forName(containerClass).getDeclaredField(fieldName);
    }

    public List<Method> getMethodsByName(String containerClass, String methodName) throws ClassNotFoundException {
        return Arrays.stream(Class.forName(containerClass).getDeclaredMethods())
                .filter(method -> method.getName().equals(methodName))
                .toList();
    }

    public List<Constructor<?>> getConstructorsByName(String containerClass) throws ClassNotFoundException {
        return Arrays.asList(Class.forName(containerClass).getDeclaredConstructors());
    }

    public Class<?> getClassByName(String clazz) throws ClassNotFoundException {
        return Class.forName(clazz);
    }
}
