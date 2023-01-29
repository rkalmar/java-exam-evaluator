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
 * - that maximum score will be distributed proportionally among checked items.
 * - if type and name are not correct, field worth 0 score
 * <p>
 * Example:
 *
 * \@FieldCheck(checkModifiers = true, score = 2)
 * \@Getter private final String testField;
 * <p>
 * Check will calculate in the following way:
 * - if field name and type is correct: +1 score
 * - if checkModifiers = true and modifiers (private, final) are added: +1 score
 * <p>
 * In this case there is 2 checked items, so each item's value is calculated by the following formula:
 * itemScore = score / checkedItemCounts = 2 / 2 = 1
 * <p>
 * So if first condition is met, then the item worth 1, if score is 2.
 */
@SyntaxCheck
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(RepeatableFieldCheck.class)
public @interface FieldCheck {

    /**
     * @return true if check needs to validate field modifiers
     */
    boolean checkModifiers() default DefaultFieldCheck.checkModifiers;

    /**
     * Defines the maximum possible score.
     * Note: this score can be obtained if all condition is satisfied.
     *
     * @return maximum score
     */
    double score() default 1;
}
