package hu.sed.evaluator.item;

import com.google.inject.Singleton;
import hu.sed.evaluator.annotation.semantic.CustomTest;
import hu.sed.evaluator.annotation.syntax.ConstructorCheck;
import hu.sed.evaluator.annotation.syntax.FieldCheck;
import hu.sed.evaluator.annotation.syntax.MethodCheck;
import hu.sed.evaluator.annotation.syntax.TypeCheck;
import hu.sed.evaluator.item.element.TypeDefinition;
import hu.sed.evaluator.item.semantic.TestItem;
import hu.sed.evaluator.item.syntax.FieldItem;
import hu.sed.evaluator.item.syntax.MethodItem;
import hu.sed.evaluator.item.syntax.TypeItem;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Singleton
public final class ItemFactory {

    public TypeItem createItem(TypeCheck check, Class<?> clazz) {
        boolean checkParentClazz = clazz.getSuperclass() != null && check.checkParentClazz();
        return TypeItem.builder()
                .checkModifiers(check.checkModifiers())
                .modifiers(clazz.getModifiers())
                .name(clazz.getCanonicalName())
                .checkParentClazz(checkParentClazz)
                .parentClazz(checkParentClazz ? clazz.getSuperclass().getCanonicalName() : null)
                .checkInterfaces(check.checkInterfaces())
                .implementedInterfaces(Arrays.stream(clazz.getInterfaces()).map(Class::getCanonicalName).toArray(String[]::new))
                .points(check.maxPoint())
                .build();
    }

    public MethodItem createItem(ConstructorCheck constructorCheck, Constructor<?> constructor) {
        return MethodItem.builder()
                .parameters(buildParameterizedTypeFromList(Arrays.asList(constructor.getGenericParameterTypes())))
                .exceptions(buildParameterizedTypeFromList(Arrays.asList(constructor.getGenericExceptionTypes())))
                .modifiers(constructor.getModifiers())
                .checkModifiers(constructorCheck.checkModifiers())
                .checkExceptions(constructorCheck.checkExceptions())
                .constructor(true)
                .containerClass(constructor.getDeclaringClass().getCanonicalName())
                .points(constructorCheck.maxPoint())
                .build();
    }

    public MethodItem createItem(MethodCheck methodCheck, Method method) {
        return MethodItem.builder()
                .name(method.getName())
                .returnType(
                        TypeDefinition.builder()
                                .type(method.getReturnType().getCanonicalName())
                                .genericTypes(buildParameterizedType(method.getGenericReturnType()))
                                .build()
                )
                .parameters(buildParameterizedTypeFromList(Arrays.asList(method.getGenericParameterTypes())))
                .exceptions(buildParameterizedTypeFromList(Arrays.asList(method.getGenericExceptionTypes())))
                .modifiers(method.getModifiers())
                .checkModifiers(methodCheck.checkModifiers())
                .checkExceptions(methodCheck.checkExceptions())
                .checkOverride(methodCheck.checkOverride())
                .containerClass(method.getDeclaringClass().getCanonicalName())
                .points(methodCheck.maxPoint())
                .build();
    }


    public FieldItem createItem(FieldCheck fieldCheck, Field field) {
        return FieldItem.builder()
                .name(field.getName())
                .type(
                        TypeDefinition.builder()
                                .type(field.getType().getCanonicalName())
                                .genericTypes(buildParameterizedType(field.getGenericType()))
                                .build()
                )
                .modifiers(field.getModifiers())
                .checkModifiers(fieldCheck.checkModifiers())
                .points(fieldCheck.maxPoint())
                .containerClass(field.getDeclaringClass().getCanonicalName())
                .build();
    }

    public TestItem createTestItem(CustomTest customTest) {
        return TestItem.builder()
                .testClass(customTest.testClass().getCanonicalName())
                .testMethods(customTest.method())
                .description(customTest.description())
                .points(customTest.maxPoint())
                .build();
    }

    private TypeDefinition[] buildParameterizedTypeFromList(List<java.lang.reflect.Type> types) {
        List<TypeDefinition> result = new ArrayList<>();
        for (java.lang.reflect.Type type : types) {
            result.add(TypeDefinition.builder()
                    .type(toTypeName(type))
                    .genericTypes(buildParameterizedType(type))
                    .build());
        }
        return result.toArray(TypeDefinition[]::new);
    }

    private TypeDefinition[] buildParameterizedType(java.lang.reflect.Type type) {
        List<TypeDefinition> result = new ArrayList<>();
        if (type instanceof ParameterizedType parameterizedType) {
            for (java.lang.reflect.Type typeArgument : parameterizedType.getActualTypeArguments()) {
                result.add(TypeDefinition.builder()
                        .type(toTypeName(typeArgument))
                        .genericTypes(buildParameterizedType(typeArgument))
                        .build());
            }
        }
        return result.toArray(TypeDefinition[]::new);
    }

    private String toTypeName(java.lang.reflect.Type type) {
        //noinspection rawtypes
        return type instanceof ParameterizedType parameterizedType1 ?
                parameterizedType1.getRawType().getTypeName() : ((Class) type).getCanonicalName();
    }
}
