package io.github.rezeros.aop;

import org.aopalliance.intercept.MethodInterceptor;

/**
 * @Author: ReZero
 * @Date: 4/9/19 3:00 PM
 * @Version 1.0
 */
public interface Advice extends MethodInterceptor {
    Pointcut getPointcut();
}
