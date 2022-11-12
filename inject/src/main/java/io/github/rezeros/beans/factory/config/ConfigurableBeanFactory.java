package io.github.rezeros.beans.factory.config;

import java.util.List;

/**
 * @Author: ReZero
 * @Date: 3/18/19 1:16 PM
 * @Version 1.0
 */
public interface ConfigurableBeanFactory extends AutowireCapableBeanFactory {

    void setBeanClassLoader(ClassLoader beanClassloader);
    ClassLoader getBeanClassLoader();
    void addBeanPostProcessor(BeanPostProcessor postProcessor);
    List<BeanPostProcessor> getBeanPostProcessors();

}
