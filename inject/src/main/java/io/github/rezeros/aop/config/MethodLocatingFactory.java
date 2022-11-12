package io.github.rezeros.aop.config;

import io.github.rezeros.beans.BeanUtils;
import io.github.rezeros.beans.factory.BeanFactory;
import io.github.rezeros.beans.factory.BeanFactoryAware;
import io.github.rezeros.beans.factory.FactoryBean;
import io.github.rezeros.util.StringUtils;

import java.lang.reflect.Method;

/**
 * @Author: ReZero
 * @Date: 4/9/19 12:03 PM
 * @Version 1.0
 */
public class MethodLocatingFactory  implements FactoryBean<Method>, BeanFactoryAware {

    private String targetBeanName;

    private String methodName;

    private Method method;

    public void setTargetBeanName(String targetBeanName) {
        this.targetBeanName = targetBeanName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }


    @Override
    public void setBeanFactory(BeanFactory beanFactory) {
        if (!StringUtils.hasText(this.targetBeanName)) {
            throw new IllegalArgumentException("Property 'targetBeanName' is required");
        }
        if (!StringUtils.hasText(this.methodName)) {
            throw new IllegalArgumentException("Property 'methodName' is required");
        }

        Class<?> beanClass = beanFactory.getType(this.targetBeanName);
        if (beanClass == null) {
            throw new IllegalArgumentException("Can't determine type of bean with name '" + this.targetBeanName + "'");
        }

        this.method = BeanUtils.resolveSignature(this.methodName, beanClass);

        if (this.method == null) {
            throw new IllegalArgumentException("Unable to locate method [" + this.methodName +
                    "] on bean [" + this.targetBeanName + "]");
        }
    }


    @Override
    public Method getObject() {
        return this.method;
    }

    @Override
    public Class<?> getObjectType() {
        return Method.class;
    }
}
