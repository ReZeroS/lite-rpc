package lite.summer.beans.factory;

import lite.summer.beans.BeanDefinition;

public interface BeanFactory {
    Object getBean(String beanID);
}
