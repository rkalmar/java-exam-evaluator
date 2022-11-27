package hu.sed.evaluator.task.item;

import hu.sed.evaluator.annotation.syntax.ConstructorCheck;
import hu.sed.evaluator.annotation.syntax.FieldCheck;
import hu.sed.evaluator.annotation.syntax.MethodCheck;
import hu.sed.evaluator.annotation.syntax.TypeCheck;
import hu.sed.evaluator.task.item.element.Type;
import hu.sed.evaluator.task.item.syntax.FieldItem;
import hu.sed.evaluator.task.item.syntax.MethodItem;
import hu.sed.evaluator.task.item.syntax.TypeItem;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class ItemFactory {

    public TypeItem createTypeItem(TypeCheck check, java.lang.reflect.Type type) {
        if (!(type instanceof Class)) {
            throw new IllegalArgumentException("Cannot create TypeItem from class: " + type.getTypeName());
        }
        Class<?> clazz = (Class<?>) type;
        return TypeItem.builder()
                .checkModifiers(check.checkModifiers())
                .modifiers(clazz.getModifiers())
                .name(clazz.getSuperclass().getCanonicalName())
                .checkParentClazz(check.checkParentClazz())
                .parentClazz(clazz.getSuperclass().getCanonicalName())
                .checkInterfaces(check.checkInterfaces())
                .implementedInterfaces(Arrays.stream(clazz.getInterfaces()).map(Class::getCanonicalName).toArray(String[]::new))
                .points(check.maxPoint())
                .build();
    }

    public MethodItem createConstructorItem(ConstructorCheck constructorCheck, Constructor constructor) {
        return MethodItem.builder()
                .parameters(buildParameterizedTypeFromList(Arrays.asList(constructor.getGenericParameterTypes())))
                .exceptions(buildParameterizedTypeFromList(Arrays.asList(constructor.getGenericExceptionTypes())))
                .modifiers(constructor.getModifiers())
                .checkModifiers(constructorCheck.checkModifiers())
                .checkExceptions(constructorCheck.checkExceptions())
                .constructor(true)
                .points(constructorCheck.maxPoint())
                .build();
    }

    public MethodItem createMethodItem(MethodCheck methodCheck, Method method) {
        return MethodItem.builder()
                .name(method.getName())
                .returnType(
                        Type.builder()
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
                .points(methodCheck.maxPoint())
                .build();
    }


    public FieldItem createFieldItem(FieldCheck fieldCheck, Field field) {
        return FieldItem.builder()
                .name(field.getName())
                .type(
                        Type.builder()
                                .type(field.getType().getCanonicalName())
                                .genericTypes(buildParameterizedType(field.getGenericType()))
                                .build()
                )
                .modifiers(field.getModifiers())
                .checkModifiers(fieldCheck.checkModifiers())
                .points(fieldCheck.maxPoint())
                .build();
    }

    private Type[] buildParameterizedTypeFromList(List<java.lang.reflect.Type> types) {
        List<Type> result = new ArrayList<>();
        for (java.lang.reflect.Type type : types) {
            result.add(Type.builder()
                    .type(toTypeName(type))
                    .genericTypes(buildParameterizedType(type))
                    .build());
        }
        return result.toArray(Type[]::new);
    }

    private Type[] buildParameterizedType(java.lang.reflect.Type type) {
        List<Type> result = new ArrayList<>();
        if (type instanceof ParameterizedType parameterizedType) {
            for (java.lang.reflect.Type typeArgument : parameterizedType.getActualTypeArguments()) {
                result.add(Type.builder()
                        .type(toTypeName(typeArgument))
                        .genericTypes(buildParameterizedType(typeArgument))
                        .build());
            }
        }
        return result.toArray(Type[]::new);
    }

    private String toTypeName(java.lang.reflect.Type type) {
        //noinspection rawtypes
        return type instanceof ParameterizedType parameterizedType1 ?
                parameterizedType1.getRawType().getTypeName() : ((Class) type).getCanonicalName();
    }


}
