package lite.summer.beans.factory.config;

import lite.summer.beans.factory.BeanFactory;

/**
 * @Author: ReZero
 * @Date: 4/7/19 11:49 PM
 * @Version 1.0
 */
public interface AutowireCapableBeanFactory extends BeanFactory {
    Object resolveDependency(DependencyDescriptor descriptor);
}
