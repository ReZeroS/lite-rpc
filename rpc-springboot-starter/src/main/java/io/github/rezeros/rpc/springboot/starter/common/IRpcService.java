package io.github.rezeros.rpc.springboot.starter.common;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * @Author linhao
 * @Date created in 7:27 下午 2022/3/7
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface IRpcService {

    int limit() default 0;

    String group() default "default";

    String serviceToken() default "";

}
