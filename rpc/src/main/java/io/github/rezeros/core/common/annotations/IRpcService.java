package io.github.rezeros.core.common.annotations;


import java.lang.annotation.*;

/**
 * @Author linhao
 * @Date created in 7:27 下午 2022/3/7
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface IRpcService {

    int limit() default 0;

    String group() default "default";

}
