package io.github.rezeros.aop.framework;

import io.github.rezeros.aop.Advice;

import java.lang.reflect.Method;
import java.util.List;

/**
 * @Author: ReZero
 * @Date: 4/9/19 5:21 PM
 * @Version 1.0
 */

public interface AopConfig  {


    Class<?> getTargetClass();

    Object getTargetObject();

    boolean isProxyTargetClass();


    Class<?>[] getProxiedInterfaces();


    boolean isInterfaceProxied(Class<?> intf);


    List<Advice> getAdvices();


    void addAdvice(Advice advice) ;

    List<Advice> getAdvices(Method method/*,Class<?> targetClass*/);

    void setTargetObject(Object obj);


}
