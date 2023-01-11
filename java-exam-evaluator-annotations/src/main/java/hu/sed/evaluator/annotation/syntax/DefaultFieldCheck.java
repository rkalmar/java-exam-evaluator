package hu.sed.evaluator.annotation.syntax;

import java.lang.annotation.Annotation;

public final class DefaultFieldCheck {
    public static final boolean checkModifiers = true;

    public static FieldCheck defaultCheck(int score) {
        return new FieldCheck() {
            @Override
            public boolean checkModifiers() {
                return checkModifiers;
            }

            @Override
            public int score() {
                return score;
            }

            @Override
            public Class<? extends Annotation> annotationType() {
                return FieldCheck.class;
            }
        };
    }
}
