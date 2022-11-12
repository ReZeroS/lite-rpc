package io.github.rezeros.test.v4;

import io.github.rezeros.dao.v4.AccountDao;
import io.github.rezeros.service.v4.PetStoreService;
import io.github.rezeros.beans.factory.config.DependencyDescriptor;
import io.github.rezeros.beans.factory.support.DefaultBeanFactory;
import io.github.rezeros.beans.factory.xml.XmlBeanDefinitionReader;
import io.github.rezeros.core.io.ClassPathResource;
import io.github.rezeros.core.io.Resource;
import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Field;

/**
 * @Author: ReZero
 * @Date: 4/7/19 11:45 PM
 * @Version 1.0
 */
public class DependencyDescriptorTest {

    @Test
    public void testResolveDependency() throws Exception{

        DefaultBeanFactory factory = new DefaultBeanFactory();
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(factory);
        Resource resource = new ClassPathResource("petstore-v4.xml");
        reader.loadBeanDefinitions(resource);

        Field f = PetStoreService.class.getDeclaredField("accountDao");
        DependencyDescriptor descriptor = new DependencyDescriptor(f,true);
        Object o = factory.resolveDependency(descriptor);
        Assert.assertTrue(o instanceof AccountDao);
    }


}
