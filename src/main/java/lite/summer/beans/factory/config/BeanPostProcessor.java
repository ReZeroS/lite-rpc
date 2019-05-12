package lite.summer.beans.factory.config;

import lite.summer.beans.BeansException;

/**
 * @Author: ReZero
 * @Date: 4/8/19 6:01 PM
 * @Version 1.0
 */
public interface BeanPostProcessor {

    Object beforeInitialization(Object bean, String beanName) throws BeansException;


    Object afterInitialization(Object bean, String beanName) throws BeansException;
}
