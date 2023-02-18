package hu.sed.evaluator.annotation.semantic.repeatable;

import hu.sed.evaluator.annotation.semantic.CustomTest;
import hu.sed.evaluator.annotation.semantic.SemanticTest;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Repeatable annotation for CustomTest.
 */
@SemanticTest
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface RepeatableCustomTest {
    /**
     * @see hu.sed.evaluator.annotation.semantic.CustomTest
     * @return repeated CustomTest array.
     */
    CustomTest[] value() default {};
}
