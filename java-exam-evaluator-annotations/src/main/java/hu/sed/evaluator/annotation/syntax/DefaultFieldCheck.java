package hu.sed.evaluator.annotation.syntax;

import java.lang.annotation.Annotation;

public final class DefaultFieldCheck {
    public static final boolean checkModifiers = true;

    public static FieldCheck defaultCheck() {
        return new FieldCheck() {
            @Override
            public boolean checkModifiers() {
                return checkModifiers;
            }

            @Override
            public double score() {
                return -1;
            }

            @Override
            public Class<? extends Annotation> annotationType() {
                return FieldCheck.class;
            }
        };
    }
}
