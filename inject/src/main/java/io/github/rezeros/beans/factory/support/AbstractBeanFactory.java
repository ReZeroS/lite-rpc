package io.github.rezeros.beans.factory.support;

import io.github.rezeros.beans.BeanDefinition;
import io.github.rezeros.beans.factory.BeanCreationException;
import io.github.rezeros.beans.factory.config.ConfigurableBeanFactory;

/**
 * @Author: ReZero
 * @Date: 4/15/19 5:17 PM
 * @Version 1.0
 */

public abstract class AbstractBeanFactory extends DefaultSingletonBeanRegistry implements ConfigurableBeanFactory {
    protected abstract Object createBean(BeanDefinition bd) throws BeanCreationException;
}
