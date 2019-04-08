package lite.summer.beans.factory.config;

import lite.summer.beans.factory.BeanFactory;

import java.util.List;

/**
 * @Author: ReZero
 * @Date: 3/18/19 1:16 PM
 * @Version 1.0
 */
public interface ConfigurableBeanFactory extends AutowireCapableBeanFactory {

    void setBeanClassLoader(ClassLoader beanClassloader);
    ClassLoader getBeanClassLoader();
    void addBeanPostProcessor(BeanPostProcessor postProcessor);
    List<BeanPostProcessor> getBeanPostProcessors();

}
