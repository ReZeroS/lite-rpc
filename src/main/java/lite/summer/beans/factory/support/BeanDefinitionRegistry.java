package lite.summer.beans.factory.support;

import lite.summer.beans.BeanDefinition;

public interface BeanDefinitionRegistry {
    void registerBeanDefinition(String id, BeanDefinition beanDefinition);

    BeanDefinition getBeanDefinition(String beanID);
}


