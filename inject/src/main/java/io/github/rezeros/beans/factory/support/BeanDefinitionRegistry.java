package io.github.rezeros.beans.factory.support;

import io.github.rezeros.beans.BeanDefinition;

public interface BeanDefinitionRegistry {
    void registerBeanDefinition(String id, BeanDefinition beanDefinition);

    BeanDefinition getBeanDefinition(String beanId);
}


