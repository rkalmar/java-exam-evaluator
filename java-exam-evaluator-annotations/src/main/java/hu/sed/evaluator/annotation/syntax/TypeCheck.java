package hu.sed.evaluator.annotation.syntax;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Checks if 'Type' is implemented correctly. Applicable on Classes/InnerClasses/Interfaces/Enums too.
 * Checks parentClass, implementedInterfaces, modifiers, annotations.
 * Furthermore, it can validate all methods and fields defined in class.
 * Note:
 * - that maximum score will be distributed proportionally among checked items.
 * - if name and package are not correct, class worth 0 score
 *
 * Example:
 *
 * \@TypeCheck(checkInterfaces = true,
 *      checkModifiers = true,
 *      score = 4)
 * public abstract class MyClass extends ClassBase implements IClass {......}
 * <p>
 * Check will calculate in the following way:
 * - if class name and package is correct: +1 score
 * - if class extends ClassBase: +1 score
 * - checkInterfaces = true, so if class implements IClass: +1 score
 * - checkModifiers = true, so if modifiers (public, abstract) are added: +1 score
 *
 * <p>
 * In this case there is 4 checked items, so each item's value is calculated by the following formula:
 * itemScore = score / checkedItemCounts = 4 / 4 = 1
 *
 * So if first three conditions are met, then the item worth 3, if score is 4.
 */
@SyntaxCheck
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface TypeCheck {

    /**
     * @return true if check needs to validate implemented interfaces
     */
    boolean checkInterfaces() default true;

    /**
     * @return true if check needs to validate type modifiers
     */
    boolean checkModifiers() default true;

    /**
     * If true, check will validate each class method one by one (including constructors)
     *
     * Note:
     *  - checks only those methods which does not have method specific checks (MethodCheck) or is not skipped (SkipCheck)
     *  - each method will be considered as one standalone unit
     *  - this check will validate all possible check of a MethodCheck (modifiers, annotation, override, thrown exceptions),
     *   if you want more sophisticated check, use annotation:
     *   @see hu.sed.evaluator.annotation.syntax.MethodCheck
     *
     * @return true if method check is needed
     */
    boolean checkMethods() default false;

    /**
     * If true, check will validate each class method one by one (including constructor methods)
     *
     * Note:
     *  - checks only those fields which does not have field specific checks (FieldCheck) or is not skipped (SkipCheck)
     *  - each field will be considered as one standalone unit
     *  - this check will validate all possible check of a FieldCheck (modifiers, annotation),
     *   if you want more sophisticated check, use annotation:
     *   @see hu.sed.evaluator.annotation.syntax.FieldCheck
     *
     * @return true if field check is needed
     */
    boolean checkFields() default false;

    /**
     * Defines the maximum possible score.
     * Note: this score can be obtained if all checked item is correct.
     * Each item has value of score distributed proportionally.
     *
     * @return maximum score
     */
    double score() default 1;
}
