package lite.summer.test.v1;

import lite.summer.beans.factory.BeanCreationException;
import lite.summer.beans.factory.BeanDefinitionStoreException;
import lite.summer.beans.factory.support.DefaultBeanFactory;
import lite.summer.beans.BeanDefinition;
import lite.summer.beans.factory.xml.XmlBeanDefinitionReader;
import lite.summer.core.io.ClassPathResource;
import lite.summer.core.io.Resource;
import lite.summer.service.v1.PetStoreService;
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
        xmlBeanDefinitionReader.loadBeanDefinition(resource);

        BeanDefinition beanDefinition = factory.getBeanDefinition("petStore");
        assertEquals("lite.summer.service.v1.PetStoreService", beanDefinition.getBeanClassName());

        assertTrue(beanDefinition.isSingleton());
        assertFalse(beanDefinition.isPrototype());

        assertEquals(BeanDefinition.SCOPE_DEFAULT, beanDefinition.getScope());
        assertEquals("lite.summer.service.v1.PetStoreService", beanDefinition.getBeanClassName());

        PetStoreService petStoreService = (PetStoreService) factory.getBean("petStore");
        assertNotNull(petStoreService);

        PetStoreService petStoreService2 = (PetStoreService) factory.getBean("petStore");
        assertEquals(petStoreService, petStoreService2);
    }

    @Test
    public void testInvalidBean(){
        try {
            Resource resource = new ClassPathResource("petstore-v1.xml");
            xmlBeanDefinitionReader.loadBeanDefinition(resource);

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
            xmlBeanDefinitionReader.loadBeanDefinition(resource);
        } catch (BeanDefinitionStoreException e) {
            return;
        }

        Assert.fail("expect BeanDefinitionStoreException ");
    }




}
