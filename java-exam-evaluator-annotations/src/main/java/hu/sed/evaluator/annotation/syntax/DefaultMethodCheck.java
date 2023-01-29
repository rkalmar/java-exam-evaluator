package hu.sed.evaluator.annotation.syntax;

import java.lang.annotation.Annotation;

public final class DefaultMethodCheck {
    public static final boolean checkModifiers = true;
    public static final boolean checkOverride = false;
    public static final boolean checkExceptions = true;

    public static MethodCheck defaultCheck() {
        return new MethodCheck() {
            @Override
            public boolean checkModifiers() {
                return checkModifiers;
            }

            @Override
            public boolean checkOverrideAnnotation() {
                return checkOverride;
            }

            @Override
            public boolean checkExceptions() {
                return checkExceptions;
            }

            @Override
            public double score() {
                return -1;
            }

            @Override
            public Class<? extends Annotation> annotationType() {
                return MethodCheck.class;
            }
        };
    }
}
