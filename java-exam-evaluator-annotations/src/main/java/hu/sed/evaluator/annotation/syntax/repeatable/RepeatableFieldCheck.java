package hu.sed.evaluator.annotation.syntax.repeatable;

import hu.sed.evaluator.annotation.syntax.FieldCheck;
import hu.sed.evaluator.annotation.syntax.SyntaxCheck;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Repeatable annotation for FieldCheck.
 */
@SyntaxCheck
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RepeatableFieldCheck {
    /**
     * @see hu.sed.evaluator.annotation.syntax.FieldCheck
     * @return repeated CustomTests array.
     */
    FieldCheck[] value() default {};
}
