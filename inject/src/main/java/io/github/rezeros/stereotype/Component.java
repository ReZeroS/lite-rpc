package io.github.rezeros.stereotype;

import java.lang.annotation.*;

/**
 * @Author: ReZero
 * @Date: 4/2/19 10:10 PM
 * @Version 1.0
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Component {

    /**
     * The value may indicate a suggestion for a logical component name,
     * to be turned into a Spring bean in case of an autodetected component.
     * @return the suggested component name, if any
     */
    String value() default "";


}
