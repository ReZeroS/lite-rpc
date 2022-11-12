package io.github.rezeros.test.v5;

import io.github.rezeros.aop.config.AspectInstanceFactory;
import io.github.rezeros.beans.factory.BeanFactory;
import io.github.rezeros.beans.factory.support.DefaultBeanFactory;
import io.github.rezeros.beans.factory.xml.XmlBeanDefinitionReader;
import io.github.rezeros.core.io.ClassPathResource;
import io.github.rezeros.core.io.Resource;
import io.github.rezeros.tx.TransactionManager;

import java.lang.reflect.Method;

/**
 * @Author: ReZero
 * @Date: 4/9/19 8:26 PM
 * @Version 1.0
 */
public class AbstractTestBaseNeeded {

    protected BeanFactory getBeanFactory(String configFile) {
        DefaultBeanFactory defaultBeanFactory = new DefaultBeanFactory();
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(defaultBeanFactory);
        Resource resource = new ClassPathResource(configFile);
        reader.loadBeanDefinitions(resource);
        return defaultBeanFactory;
    }

    protected Method getAdviceMethod(String methodName) throws Exception {
        return TransactionManager.class.getMethod(methodName);
    }

    protected AspectInstanceFactory getAspectInstanceFactory(String targetBeanName) {
        AspectInstanceFactory factory = new AspectInstanceFactory();
        factory.setAspectBeanName(targetBeanName);
        return factory;
    }

}
