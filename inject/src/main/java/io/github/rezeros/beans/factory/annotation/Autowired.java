package io.github.rezeros.beans.factory.annotation;

import java.lang.annotation.*;

/**
 * @Author: ReZero
 * @Date: 4/2/19 10:21 PM
 * @Version 1.0
 */

@Target({ElementType.CONSTRUCTOR, ElementType.FIELD, ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Autowired {

    /**
     * Declares whether the annotated dependency is required.
     * <p>Defaults to {@code true}.
     */
    boolean required() default true;

}

