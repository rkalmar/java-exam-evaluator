package hu.sed.evaluator.annotation.syntax;

import hu.sed.evaluator.annotation.syntax.repeatable.RepeatableConstructorCheck;


import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@SyntaxCheck
@Target(ElementType.CONSTRUCTOR)
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(RepeatableConstructorCheck.class)
public @interface ConstructorCheck {

    /**
     * @return true if check needs to validate method modifiers
     */
    boolean checkModifiers() default true;

    /**
     * @return true if check needs to validate checked exceptions to be thrown
     */
    boolean checkExceptions() default true;

    /**
     * Defines the maximum possible point.
     * Note: this point can be obtained if all checked value is correct.
     *
     * @return maximum point
     */
    int maxPoint() default 1;
}
