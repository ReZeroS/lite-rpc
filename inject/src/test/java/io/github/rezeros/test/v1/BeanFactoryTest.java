package io.github.rezeros.test.v1;

import io.github.rezeros.beans.factory.BeanCreationException;
import io.github.rezeros.beans.factory.BeanDefinitionStoreException;
import io.github.rezeros.beans.factory.support.DefaultBeanFactory;
import io.github.rezeros.beans.BeanDefinition;
import io.github.rezeros.beans.factory.xml.XmlBeanDefinitionReader;
import io.github.rezeros.core.io.ClassPathResource;
import io.github.rezeros.core.io.Resource;
import io.github.rezeros.service.v1.PetStoreService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class BeanFactoryTest {

    DefaultBeanFactory factory = null;
    XmlBeanDefinitionReader xmlBeanDefinitionReader = null;



    @Before
    public void setUp(){
        factory = new DefaultBeanFactory();
        xmlBeanDefinitionReader = new XmlBeanDefinitionReader(factory);
    }


    @Test
    public void testGetBean(){
        Resource resource = new ClassPathResource("petstore-v1.xml");
        xmlBeanDefinitionReader.loadBeanDefinitions(resource);

        BeanDefinition beanDefinition = factory.getBeanDefinition("petStore");
        assertEquals("io.github.rezeros.service.v1.PetStoreService", beanDefinition.getBeanClassName());

        assertTrue(beanDefinition.isSingleton());
        assertFalse(beanDefinition.isPrototype());

        assertEquals(BeanDefinition.SCOPE_DEFAULT, beanDefinition.getScope());
        assertEquals("io.github.rezeros.service.v1.PetStoreService", beanDefinition.getBeanClassName());

        PetStoreService petStoreService = (PetStoreService) factory.getBean("petStore");
        assertNotNull(petStoreService);

        PetStoreService petStoreService2 = (PetStoreService) factory.getBean("petStore");
        assertEquals(petStoreService, petStoreService2);
    }

    @Test
    public void testInvalidBean(){
        try {
            Resource resource = new ClassPathResource("petstore-v1.xml");
            xmlBeanDefinitionReader.loadBeanDefinitions(resource);

            factory.getBean("invalidBean");
        } catch (BeanCreationException e) {
            return;
        }
        Assert.fail("expect BeanCreationException ");
    }

    @Test
    public void testInvalidXML(){
        try {
            Resource resource = new ClassPathResource("xxx.xml");
            xmlBeanDefinitionReader.loadBeanDefinitions(resource);
        } catch (BeanDefinitionStoreException e) {
            return;
        }

        Assert.fail("expect BeanDefinitionStoreException ");
    }




}
