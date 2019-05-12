package lite.summer.beans.factory;

import lite.summer.beans.BeansException;

/**
 * @Author: ReZero
 * @Date: 4/15/19 4:29 PM
 * @Version 1.0
 */
public interface BeanFactoryAware {
    void setBeanFactory(BeanFactory beanFactory) throws BeansException;
}
