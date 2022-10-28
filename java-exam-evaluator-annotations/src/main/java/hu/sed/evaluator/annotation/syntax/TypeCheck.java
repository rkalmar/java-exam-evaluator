package hu.sed.evaluator.annotation.syntax;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Checks if 'Type' is implemented correctly. Applicable on Classes/InnerClasses and on Interfaces too.
 * Checks parentClass, implementedInterfaces, modifiers, annotations.
 * Furthermore, it can validate all methods and fields defined in class.
 * Note:
 * - that maximum point will be distributed proportionally among checked items.
 * - if name and package are not correct, class worth 0 points
 *
 * Example:
 *
 * @TypeCheck(checkParentClazz = true,
 *      checkInterfaces = true,
 *      checkModifiers = true,
 *      maxPoints = 4)
 * @EClass
 * public abstract class MyClass extends ClassBase implements IClass {......}
 * <p>
 * Check will calculate in the following way:
 * - if class name and package is correct: +1 point
 * - checkParentClazz = true, so if class extends ClassBase: +1 point
 * - checkInterfaces = true, so if class implements IClass: +1 point
 * - checkModifiers = true, so if modifiers (public, abstract) are added: +1 point
 *
 * <p>
 * In this case there is 4 checked items, so each item's value is calculated by the following formula:
 * itemPoint = maxPoint / checkedItemCounts = 4 / 4 = 1
 *
 * Besides,
 *   - each method worth points defined by pointsPerMethod and
 *   - each field worth points defined by pointsPerField
 *
 * So if first three conditions are met, then the item worth 3 points, if maxPoint is 4.
 * + (properly implemented method * pointsPerMethod) + (properly implemented fields * pointsPerField)
 */
@SyntaxCheck
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface TypeCheck {

    /**
     * @return true if check needs to validate parent class
     */
    boolean checkParentClazz() default true;

    /**
     * @return true if check needs to validate parent class
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
     * If checkMethods is true, and a given method does not have specific MethodCheck, then it defines points per method
     *
     * @return maximum point per method
     */
    int pointsPerMethod() default 1;

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
     * If checkFields is true, and a given field does not have specific FieldCheck, then it defines points per field
     *
     * @return maximum point per method
     */
    int pointsPerField() default 1;

    /**
     * Defines the maximum possible point.
     * Note: this point can be obtained if all checked item is correct.
     * Each item has value of point distributed proportionally.
     *
     * @return maximum point
     */
    int maxPoint() default 1;
}
