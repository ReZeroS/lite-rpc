package io.github.rezeros.beans.factory.config;

import io.github.rezeros.beans.BeansException;

/**
 * @Author: ReZero
 * @Date: 4/8/19 6:31 PM
 * @Version 1.0
 */
public interface InstantiationAwareBeanPostProcessor extends BeanPostProcessor {

    Object beforeInstantiation(Class<?> beanClass, String beanName) throws BeansException;

    boolean afterInstantiation(Object bean, String beanName) throws BeansException;

    void postProcessPropertyValues(Object bean, String beanName) throws BeansException;

}
