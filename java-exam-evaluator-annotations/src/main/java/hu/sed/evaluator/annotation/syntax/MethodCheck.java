package hu.sed.evaluator.annotation.syntax;

import hu.sed.evaluator.annotation.syntax.repeatable.RepeatableMethodCheck;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Checks if the method is implemented correctly. Applicable on Methods and Constructors too.
 * Checks modifiers, annotations, overrides (if any).
 * <p>
 * Note:
 * - that maximum point will be distributed proportionally among checked items.
 * - if return type, method name and parameter types are not correct, method worth 0 points
 * <p>
 * Example:
 *
 * @MethodCheck(checkModifiers = true,
 * checkAnnotations = true,
 * checkOverride = true,
 * checkExceptions = true,
 * maxPoint = 1)
 * @Override public static void doSomething() throws IllegalArgumentException {....}
 * <p>
 * Check will calculate in the following way:
 * - if return type, method name and args types are correct: +1 point
 * - checkModifiers = true, so if modifiers (public, static) are added: +1 point
 * - checkAnnotations = true, so if annotation is added (@Override): +1 point
 * - checkOverride = true, so if annotation is a real override from an abstract class or an interface: +1 point
 * - checkExceptions = true, so if exception is added (@IllegalArgumentException): +1 point
 * <p>
 * In this case there is 5 checked items, so each item's value is calculated by the following formula:
 * itemPoint = maxPoint / checkedItemCounts = 1 / 5
 * <p>
 * So if first three conditions are met, then the item worth 0.6 points, if maxPoint is 1.
 */
@SyntaxCheck
@Target({ElementType.METHOD, ElementType.CONSTRUCTOR})
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(RepeatableMethodCheck.class)
public @interface MethodCheck {

    /**
     * @return true if check needs to validate method modifiers
     */
    boolean checkModifiers() default true;

    /**
     * @return true if check needs to validate method annotations
     */
    boolean checkAnnotations() default false;

    /**
     * @return true if check needs to validate, if method is an override
     */
    boolean checkOverride() default false;

    /**
     * @return true if check needs to validate checked exceptions to be thrown
     */
    boolean checkExceptions() default false;

    /**
     * Defines the maximum possible point.
     * Note: this point can be obtained if all checked value is correct.
     *
     * @return maximum point
     */
    int maxPoint() default 1;
}