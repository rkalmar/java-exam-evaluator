package hu.sed.evaluator.annotation.syntax;

import java.lang.annotation.Annotation;

public final class DefaultConstructorCheck {
    public static final boolean checkModifiers = true;
    public static final boolean checkExceptions = true;

    public static ConstructorCheck defaultCheck() {
        return new ConstructorCheck() {
            @Override
            public boolean checkModifiers() {
                return checkModifiers;
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
                return ConstructorCheck.class;
            }
        };
    }
}
