package hu.sed.evaluator.annotation.syntax.repeatable;

import hu.sed.evaluator.annotation.syntax.MethodCheck;
import hu.sed.evaluator.annotation.syntax.SyntaxCheck;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Repeatable annotation for MethodCheck.
 */
@SyntaxCheck
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RepeatableMethodCheck {
    /**
     * @see hu.sed.evaluator.annotation.syntax.MethodCheck
     * @return repeated MethodCheck array.
     */
    MethodCheck[] value() default {};
}
