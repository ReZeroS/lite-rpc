package lite.summer.beans.factory;

import lite.summer.beans.BeanDefinition;

public interface BeanFactory {
    public BeanDefinition getBeanDefinition(String beanID);

    public Object getBean(String beanID);
}
