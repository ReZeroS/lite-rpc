package lite.summer.beans.factory.support;

import lite.summer.beans.BeanDefinition;
import lite.summer.beans.factory.BeanCreationException;
import lite.summer.beans.factory.config.ConfigurableBeanFactory;

/**
 * @Author: ReZero
 * @Date: 4/15/19 5:17 PM
 * @Version 1.0
 */

public abstract class AbstractBeanFactory extends DefaultSingletonBeanRegistry implements ConfigurableBeanFactory {
    protected abstract Object createBean(BeanDefinition bd) throws BeanCreationException;
}
