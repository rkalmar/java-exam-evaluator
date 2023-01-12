package hu.sed.evaluator;

import hu.sed.evaluator.annotation.semantic.CustomTest;
import hu.sed.evaluator.annotation.semantic.SemanticTest;
import hu.sed.evaluator.annotation.syntax.ConstructorCheck;
import hu.sed.evaluator.annotation.syntax.FieldCheck;
import hu.sed.evaluator.annotation.syntax.MethodCheck;
import hu.sed.evaluator.annotation.syntax.SkipCheck;
import hu.sed.evaluator.annotation.syntax.SyntaxCheck;
import lombok.experimental.UtilityClass;
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

@UtilityClass
public class ReflectionUtils {

    public List<Class<?>> getClassesOfPackage(String packageName) {
        Reflections reflections = new Reflections(packageName, Scanners.SubTypes.filterResultsBy(s -> true));
        return new ArrayList<>(reflections.getSubTypesOf(Object.class));
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

    public <T extends Annotation> Optional<T> getAnnotation(Class<T> annotationClazz, Class<?> clazz) {
        return clazz.isAnnotationPresent(annotationClazz) ? Optional.of(clazz.getAnnotation(annotationClazz)) : Optional.empty();
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

    public Field getMethodByName(String containerClass, String methodName) throws ClassNotFoundException, NoSuchMethodException {
        return Arrays.stream(Class.forName(containerClass).getDeclaredFields())
                .filter(method -> method.getName().equals(methodName))
                .findFirst()
                .orElseThrow(NoSuchMethodException::new);
    }
}
