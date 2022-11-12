package io.github.rezeros.test.v5;

import io.github.rezeros.aop.config.MethodLocatingFactory;
import io.github.rezeros.beans.factory.support.DefaultBeanFactory;
import io.github.rezeros.beans.factory.xml.XmlBeanDefinitionReader;
import io.github.rezeros.core.io.ClassPathResource;
import io.github.rezeros.core.io.Resource;
import io.github.rezeros.tx.TransactionManager;
import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Method;

/**
 * @Author: ReZero
 * @Date: 4/9/19 12:02 PM
 * @Version 1.0
 */
public class MethodLocatingFactoryTest {
    @Test
    public void testGetMethod() throws Exception{
        DefaultBeanFactory beanFactory = new DefaultBeanFactory();
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(beanFactory);
        Resource resource = new ClassPathResource("petstore-v5.xml");
        reader.loadBeanDefinitions(resource);

        MethodLocatingFactory methodLocatingFactory = new MethodLocatingFactory();
        methodLocatingFactory.setTargetBeanName("tx");
        methodLocatingFactory.setMethodName("start");
        methodLocatingFactory.setBeanFactory(beanFactory);

        Method m = methodLocatingFactory.getObject();

        Assert.assertTrue(TransactionManager.class.equals(m.getDeclaringClass()));
        Assert.assertTrue(m.equals(TransactionManager.class.getMethod("start")));

    }
}
