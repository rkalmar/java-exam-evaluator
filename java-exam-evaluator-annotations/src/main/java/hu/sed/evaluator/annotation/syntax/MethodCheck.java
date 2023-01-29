package hu.sed.evaluator.annotation.syntax;

import hu.sed.evaluator.annotation.syntax.repeatable.RepeatableMethodCheck;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Checks if the method is implemented correctly. Applicable on Methods.
 * Checks modifiers, annotations, overrides (if any).
 * <p>
 * Note:
 * - that maximum score will be distributed proportionally among checked items.
 * - if return type, method name and parameter types are not correct, method worth 0 score
 * <p>
 * Example:
 *
 * \@MethodCheck(checkModifiers = true,
 * checkExceptions = true,
 * score = 1)
 * \@Override public static void doSomething() throws IllegalArgumentException {....}
 * <p>
 * Check will calculate in the following way:
 * - if return type, method name and args types are correct: +1 score
 * - checkModifiers = true, so if modifiers (public, static) are added: +1 score
 * - checkExceptions = true, so if exception is added (@IllegalArgumentException): +1 score
 * <p>
 * In this case there is 3 checked items, so each item's value is calculated by the following formula:
 * itemScore = score / checkedItemCounts = 1 / 3 = 0.33
 * <p>
 * So if first two conditions are met, then the item worth 0.66, if score is 1.
 */
@SyntaxCheck
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(RepeatableMethodCheck.class)
public @interface MethodCheck {

    /**
     * @return true if check needs to validate method modifiers
     */
    boolean checkModifiers() default DefaultMethodCheck.checkModifiers;

    /**
     * @return true if check needs to validate checked exceptions to be thrown
     */
    boolean checkExceptions() default DefaultMethodCheck.checkExceptions;

    /**
     * Defines the maximum possible score.
     * Note: this score can be obtained if all checked value is correct.
     *
     * @return maximum score
     */
    double score() default 1;
}
