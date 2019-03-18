package lite.summer.beans.factory.config;

import lite.summer.beans.factory.BeanFactory;

/**
 * @Author: ReZero
 * @Date: 3/18/19 1:16 PM
 * @Version 1.0
 */
public interface ConfigurableBeanFactory extends BeanFactory {

    void setBeanClassLoader(ClassLoader beanClassloader);
    ClassLoader getBeanClassLoader();

}
