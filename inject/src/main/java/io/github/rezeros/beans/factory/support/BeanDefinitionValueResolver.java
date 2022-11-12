package io.github.rezeros.beans.factory.support;

import io.github.rezeros.beans.BeanDefinition;
import io.github.rezeros.beans.BeansException;
import io.github.rezeros.beans.factory.BeanCreationException;
import io.github.rezeros.beans.factory.FactoryBean;
import io.github.rezeros.beans.factory.config.RuntimeBeanReference;
import io.github.rezeros.beans.factory.config.TypedStringValue;

/**
 * @Author: ReZero
 * @Date: 3/31/19 9:12 PM
 * @Version 1.0
 */
public class BeanDefinitionValueResolver {
    private final AbstractBeanFactory beanFactory;

    public BeanDefinitionValueResolver(AbstractBeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    public Object resolveValueIfNecessary(Object value) {

//        return propertyValue.resolve(this.beanFactory);

        if (value instanceof RuntimeBeanReference) {
            RuntimeBeanReference ref = (RuntimeBeanReference) value;
            String refName = ref.getBeanName();
            Object bean = this.beanFactory.getBean(refName);
            return bean;
        } else if (value instanceof TypedStringValue) {
            return ((TypedStringValue) value).getValue();
        } else if (value instanceof BeanDefinition) {
            // Resolve plain BeanDefinition, without contained name: use dummy name.
            BeanDefinition beanDefinition = (BeanDefinition) value;

            String innerBeanName = "(inner bean)" + beanDefinition.getBeanClassName() + "#" +
                    Integer.toHexString(System.identityHashCode(beanDefinition));
            return resolveInnerBean(innerBeanName, beanDefinition);
        } else {
            return value;
        }
    }

    private Object resolveInnerBean(String innerBeanName, BeanDefinition innerBeanDefinition) {
        try {
            Object innerBean = this.beanFactory.createBean(innerBeanDefinition);
            if (innerBean instanceof FactoryBean) {
                try {
                    return ((FactoryBean<?>) innerBean).getObject();
                } catch (Exception e) {
                    throw new BeanCreationException(innerBeanName, "FactoryBean threw exception on object creation", e);
                }
            } else {
                return innerBean;
            }
        } catch (BeansException ex) {
            throw new BeanCreationException(
                    innerBeanName,
                    "Cannot create inner bean '" + innerBeanName + "' " +
                            (innerBeanDefinition != null && innerBeanDefinition.getBeanClassName() != null ? "of type [" + innerBeanDefinition.getBeanClassName() + "] " : "")
                    , ex);
        }
    }
}
