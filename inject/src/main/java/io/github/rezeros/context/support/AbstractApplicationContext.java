package io.github.rezeros.context.support;

import io.github.rezeros.beans.factory.NoSuchBeanDefinitionException;
import io.github.rezeros.beans.factory.annotation.AutowiredAnnotationProcessor;
import io.github.rezeros.beans.factory.support.DefaultBeanFactory;
import io.github.rezeros.core.io.Resource;
import io.github.rezeros.util.ClassUtils;
import io.github.rezeros.aop.aspectj.AspectJAutoProxyProcessor;
import io.github.rezeros.beans.factory.config.ConfigurableBeanFactory;
import io.github.rezeros.beans.factory.xml.XmlBeanDefinitionReader;
import io.github.rezeros.context.ApplicationContext;

import java.util.List;

/**
 * @Author: ReZero
 * @Date: 3/18/19 9:40 AM
 * @Version 1.0
 */
public abstract class AbstractApplicationContext implements ApplicationContext {

    private ClassLoader classLoader;

    private DefaultBeanFactory factory;

    public AbstractApplicationContext(String configFile){
        factory = new DefaultBeanFactory();
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(factory);
        Resource resource = this.getResourceByPath(configFile);
        reader.loadBeanDefinitions(resource);
        factory.setBeanClassLoader(this.getBeanClassLoader());
        registerBeanPostProcessors(factory);
    }


    protected abstract Resource getResourceByPath(String path);

    @Override
    public Object getBean(String beanID) {
        return factory.getBean(beanID);
    }

    public void setBeanClassLoader(ClassLoader beanClassLoader) {
        this.classLoader = beanClassLoader;
    }

    public ClassLoader getBeanClassLoader() {
        return this.classLoader != null ? this.classLoader : ClassUtils.getDefaultClassLoader();
    }

    protected void registerBeanPostProcessors(ConfigurableBeanFactory configurableBeanFactory){
        {
            //populateBean postProcessPropertyValues
            AutowiredAnnotationProcessor autowiredAnnotationProcessor = new AutowiredAnnotationProcessor();
            autowiredAnnotationProcessor.setBeanFactory(configurableBeanFactory);
            configurableBeanFactory.addBeanPostProcessor(autowiredAnnotationProcessor);
        }
        {
            //applyBeanPostProcessorsAfterInitialization
            AspectJAutoProxyProcessor postProcessor = new AspectJAutoProxyProcessor();
            postProcessor.setBeanFactory(configurableBeanFactory);
            configurableBeanFactory.addBeanPostProcessor(postProcessor);
        }
    }

    @Override
    public Class<?> getType(String name) throws NoSuchBeanDefinitionException {
        return this.factory.getType(name);
    }

    @Override
    public List<Object> getBeansByType(Class<?> classType) {
        return this.factory.getBeansByType(classType);
    }

}
