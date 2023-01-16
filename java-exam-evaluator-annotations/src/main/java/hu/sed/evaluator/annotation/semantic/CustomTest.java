package hu.sed.evaluator.annotation.semantic;

import hu.sed.evaluator.annotation.semantic.repeatable.RepeatableCustomTest;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Custom test is for executing tests in a class. By default, all test methods will be executed,
 * but it can be changed by explicitly defining test methods in 'method' param.
 */
@SemanticTest
@Target({ElementType.TYPE, ElementType.CONSTRUCTOR, ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(RepeatableCustomTest.class)
public @interface CustomTest {
    /**
     * @return class which contains test method defined in 'method' param
     */
    Class<?> testClass();

    /**
     * @return array of methods to execute as part of the test
     */
    String[] method() default CustomTestContants.ALL_TEST;

    /**
     * @return test description
     */
    String description() default "";

    /**
     * Defines the maximum possible score.
     * Note: this score can be obtained if all test method is successful,
     * therefore it shared among test methods.
     *
     * @return maximum score
     */
    int score() default 1;
}
