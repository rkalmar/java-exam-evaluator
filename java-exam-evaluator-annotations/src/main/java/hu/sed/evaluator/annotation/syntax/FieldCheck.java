package hu.sed.evaluator.annotation.syntax;

import hu.sed.evaluator.annotation.syntax.repeatable.RepeatableFieldCheck;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Checks if a field is implemented correctly.
 * Checks modifiers and annotations (if any).
 * <p>
 * Note:
 * - that maximum point will be distributed proportionally among checked items.
 * - if type and name are not correct, field worth 0 points
 * <p>
 * Example:
 *
 * @FieldCheck(checkModifiers = true, checkAnnotations = true, maxPoint = 3)
 * @Getter private final String testField;
 * <p>
 * Check will calculate in the following way:
 * - if field name and type is correct: +1 point
 * - if checkModifiers = true and modifiers (private, final) are added: +1 point
 * - if checkAnnotations = true and annotation is added (@Getter): +1 point
 * <p>
 * In this case there is 3 checked items, so each item's value is calculated by the following formula:
 * itemPoint = maxPoint / checkedItemCounts = 3 / 3
 * <p>
 * So if first two conditions are met, then the item worth 2 points, if maxPoint is 3.
 */
@SyntaxCheck
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(RepeatableFieldCheck.class)
public @interface FieldCheck {

    /**
     * @return true if check needs to validate field modifiers
     */
    boolean checkModifiers() default true;

    /**
     * @return true if check needs to validate method annotations
     */
    boolean checkAnnotations() default false;

    /**
     * Defines the maximum possible point.
     * Note: this point can be obtained if all condition is satisfied.
     *
     * @return maximum point
     */
    int maxPoint() default 1;
}
