package hu.sed.evaluator.annotation.syntax;

import java.lang.annotation.Annotation;

public final class DefaultConstructorCheck {
    public static final boolean checkModifiers = true;
    public static final boolean checkExceptions = true;

    public static ConstructorCheck defaultCheck(int score) {
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
            public int score() {
                return score;
            }

            @Override
            public Class<? extends Annotation> annotationType() {
                return ConstructorCheck.class;
            }
        };
    }
}
