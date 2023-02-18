package hu.sed.evaluator.annotation.syntax.repeatable;

import hu.sed.evaluator.annotation.syntax.ConstructorCheck;
import hu.sed.evaluator.annotation.syntax.SyntaxCheck;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Repeatable annotation for MethodCheck.
 */
@SyntaxCheck
@Target(ElementType.CONSTRUCTOR)
@Retention(RetentionPolicy.RUNTIME)
public @interface RepeatableConstructorCheck {
    /**
     * @see hu.sed.evaluator.annotation.syntax.ConstructorCheck
     * @return repeated ConstructorCheck array.
     */
    ConstructorCheck[] value() default {};
}
