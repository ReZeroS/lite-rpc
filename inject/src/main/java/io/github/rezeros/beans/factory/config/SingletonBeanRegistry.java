package io.github.rezeros.beans.factory.config;

/**
 * @Author: ReZero
 * @Date: 3/18/19 4:02 PM
 * @Version 1.0
 */
public interface SingletonBeanRegistry {
    void registrySingleton(String beanName, Object singletonObject);

    Object getSingleton(String beanName);



}
