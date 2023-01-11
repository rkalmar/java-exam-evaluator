package hu.sed.evaluator.annotation.syntax;

import java.lang.annotation.Annotation;

public final class DefaultMethodCheck {
    public static final boolean checkModifiers = true;
    public static final boolean checkOverride = true;
    public static final boolean checkExceptions = true;

    public static MethodCheck defaultCheck(int score) {
        return new MethodCheck() {
            @Override
            public boolean checkModifiers() {
                return checkModifiers;
            }

            @Override
            public boolean checkOverride() {
                return checkOverride;
            }

            @Override
            public boolean checkExceptions() {
                return checkExceptions;
            }

            @Override
            public int score() {
                return score;
            }

            @Override
            public Class<? extends Annotation> annotationType() {
                return MethodCheck.class;
            }
        };
    }
}