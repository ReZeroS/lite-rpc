package io.github.rezeros.rpc.springboot.starter.common;

import java.lang.annotation.*;

/**
 * @Author linhao
 * @Date created in 7:28 下午 2022/3/7
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface IRpcReference {

    String url() default "";

    String group() default "default";

    String serviceToken() default "";

    int timeOut() default 3000;

    int retry() default 1;

    boolean async() default false;

}
