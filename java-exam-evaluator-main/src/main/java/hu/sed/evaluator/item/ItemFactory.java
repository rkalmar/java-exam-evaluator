package hu.sed.evaluator.item;

import com.google.inject.Singleton;
import hu.sed.evaluator.annotation.semantic.CustomTest;
import hu.sed.evaluator.annotation.syntax.ConstructorCheck;
import hu.sed.evaluator.annotation.syntax.FieldCheck;
import hu.sed.evaluator.annotation.syntax.MethodCheck;
import hu.sed.evaluator.annotation.syntax.TypeCheck;
import hu.sed.evaluator.item.element.TypeDefinition;
import hu.sed.evaluator.item.semantic.TestItem;
import hu.sed.evaluator.item.syntax.ConstructorItem;
import hu.sed.evaluator.item.syntax.FieldItem;
import hu.sed.evaluator.item.syntax.MethodItem;
import hu.sed.evaluator.item.syntax.TypeItem;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Singleton
public class ItemFactory {

    public TypeItem createItem(TypeCheck check, Class<?> clazz) {
        boolean checkParentClazz = !clazz.isInterface() && check.checkParentClazz();
        return TypeItem.builder()
                .checkModifiers(check.checkModifiers())
                .modifiers(clazz.getModifiers())
                .name(clazz.getName())
                .checkParentClazz(checkParentClazz)
                .interfaze(clazz.isInterface())
                .enumeration(clazz.isEnum())
                .parentClazz(checkParentClazz ? createTypeDef(clazz.getGenericSuperclass()) : null)
                .checkInterfaces(check.checkInterfaces())
                .implementedInterfaces(createTypeDefForImplementedInterfaces(clazz))
                .score(check.score())
                .build();
    }

    public ConstructorItem createItem(ConstructorCheck constructorCheck, Constructor<?> constructor) {
        return ConstructorItem.builder()
                .name("constructorMethod")
                .parameters(buildParameterizedTypeFromList(constructor.getGenericParameterTypes()))
                .exceptions(buildParameterizedTypeFromList(constructor.getGenericExceptionTypes()))
                .modifiers(constructor.getModifiers())
                .checkModifiers(constructorCheck.checkModifiers())
                .checkExceptions(constructorCheck.checkExceptions())
                .containerClass(constructor.getDeclaringClass().getName())
                .score(constructorCheck.score())
                .build();
    }

    public MethodItem createItem(MethodCheck methodCheck, Method method) {
        return MethodItem.builder()
                .name(method.getName())
                .returnType(createReturnTypeDef(method))
                .parameters(buildParameterizedTypeFromList(method.getGenericParameterTypes()))
                .exceptions(buildParameterizedTypeFromList(method.getGenericExceptionTypes()))
                .modifiers(method.getModifiers())
                .checkModifiers(methodCheck.checkModifiers())
                .checkExceptions(methodCheck.checkExceptions())
                .containerClass(method.getDeclaringClass().getName())
                .score(methodCheck.score())
                .build();
    }


    public FieldItem createItem(FieldCheck fieldCheck, Field field) {
        return FieldItem.builder()
                .name(field.getName())
                .type(createTypeDef(field))
                .modifiers(field.getModifiers())
                .checkModifiers(fieldCheck.checkModifiers())
                .score(fieldCheck.score())
                .containerClass(field.getDeclaringClass().getName())
                .build();
    }

    public TypeDefinition createTypeDef(Type type) {
        return TypeDefinition.builder()
                .type(toTypeName(type))
                .genericTypes(buildParameterizedType(type))
                .build();
    }

    public TypeDefinition createTypeDef(Field field) {
        return TypeDefinition.builder()
                .type(field.getType().getName())
                .genericTypes(buildParameterizedType(field.getGenericType()))
                .build();
    }

    public TypeDefinition[] createTypeDefForImplementedInterfaces(Class<?> clazz) {
        return Arrays.stream(clazz.getGenericInterfaces()).map(this::createTypeDef).toArray(TypeDefinition[]::new);
    }

    public TypeDefinition createReturnTypeDef(Method method) {
        return TypeDefinition.builder()
                .type(method.getReturnType().getName())
                .genericTypes(buildParameterizedType(method.getGenericReturnType()))
                .build();
    }

    public TestItem createTestItem(CustomTest customTest) {
        return TestItem.builder()
                .testClass(customTest.testClass().getName())
                .testMethods(customTest.method())
                .description(customTest.description())
                .score(customTest.score())
                .build();
    }

    public TypeDefinition[] buildParameterizedTypeFromList(java.lang.reflect.Type[] types) {
        List<TypeDefinition> result = new ArrayList<>();
        for (java.lang.reflect.Type type : types) {
            result.add(TypeDefinition.builder()
                    .type(toTypeName(type))
                    .genericTypes(buildParameterizedType(type))
                    .build());
        }
        return result.toArray(TypeDefinition[]::new);
    }

    public TypeDefinition[] buildParameterizedType(java.lang.reflect.Type type) {
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
        return type instanceof ParameterizedType parameterizedType ?
                parameterizedType.getRawType().getTypeName() : ((Class) type).getName();
    }
}
