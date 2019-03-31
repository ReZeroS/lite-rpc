package lite.summer.beans.factory.support;

import lite.summer.beans.factory.config.RuntimeBeanReference;
import lite.summer.beans.factory.config.TypedStringValue;

/**
 * @Author: ReZero
 * @Date: 3/31/19 9:12 PM
 * @Version 1.0
 */
public class BeanDefinitionValueResolver {
    private final DefaultBeanFactory beanFactory;

    public BeanDefinitionValueResolver(
            DefaultBeanFactory beanFactory) {

        this.beanFactory = beanFactory;
    }

    public Object resolveValueIfNecessary(Object value) {

        if (value instanceof RuntimeBeanReference) {
            RuntimeBeanReference ref = (RuntimeBeanReference) value;
            String refName = ref.getBeanName();
            Object bean = this.beanFactory.getBean(refName);
            return bean;

        }else if (value instanceof TypedStringValue) {
            return ((TypedStringValue) value).getValue();
        } else{
            //TODO
            throw new RuntimeException("the value " + value +" has not implemented");
        }
    }
}
