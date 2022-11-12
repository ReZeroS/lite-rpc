package io.github.rezeros.beans.factory;

import java.util.List;

public interface BeanFactory {
    Object getBean(String beanID);

    Class<?> getType(String targetBeanName);

    List<Object> getBeansByType(Class<?> classType);

}
