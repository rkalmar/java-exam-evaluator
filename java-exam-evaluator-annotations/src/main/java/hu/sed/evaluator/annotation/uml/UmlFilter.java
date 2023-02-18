package hu.sed.evaluator.annotation.uml;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface UmlFilter {

    /**
     * Methods startsWith any from the array will be filtered from UML
     * @return array of filter strings
     */
    String[] methodPrefixes() default {};
}
