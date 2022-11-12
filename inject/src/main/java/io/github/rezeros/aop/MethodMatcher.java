package io.github.rezeros.aop;

import java.lang.reflect.Method;

/**
 * @Author: ReZero
 * @Date: 4/8/19 10:14 PM
 * @Version 1.0
 */
public interface MethodMatcher {

    boolean matches(Method method/*, Class<?> targetClass*/);

}
