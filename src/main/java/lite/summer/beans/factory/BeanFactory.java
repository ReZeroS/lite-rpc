package lite.summer.beans.factory;

import lite.summer.aop.Advice;
import lite.summer.beans.BeanDefinition;

import java.util.List;

public interface BeanFactory {
    Object getBean(String beanID);

    Class<?> getType(String targetBeanName);

    List<Object> getBeansByType(Class<?> classType);

}
