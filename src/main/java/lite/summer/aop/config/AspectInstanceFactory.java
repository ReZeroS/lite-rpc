package lite.summer.aop.config;

import lite.summer.beans.factory.BeanFactory;
import lite.summer.beans.factory.BeanFactoryAware;
import lite.summer.util.StringUtils;

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

