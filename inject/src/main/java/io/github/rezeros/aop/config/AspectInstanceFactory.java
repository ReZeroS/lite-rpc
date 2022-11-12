package io.github.rezeros.aop.config;

import io.github.rezeros.beans.factory.BeanFactory;
import io.github.rezeros.beans.factory.BeanFactoryAware;
import io.github.rezeros.util.StringUtils;

/**
 * @Author: ReZero
 * @Date: 4/15/19 4:27 PM
 * @Version 1.0
 */
public class AspectInstanceFactory implements BeanFactoryAware {

    private String aspectBeanName;

    private BeanFactory beanFactory;

    public void setAspectBeanName(String aspectBeanName) {
        this.aspectBeanName = aspectBeanName;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
        if (!StringUtils.hasText(this.aspectBeanName)) {
            throw new IllegalArgumentException("'aspectBeanName' is required");
        }
    }

    public Object getAspectInstance() {
        return this.beanFactory.getBean(this.aspectBeanName);
    }
}

