package lite.summer.test.v1;

import lite.summer.beans.factory.BeanCreationException;
import lite.summer.beans.factory.BeanFactory;
import lite.summer.beans.factory.support.DefaultBeanFactory;
import lite.summer.beans.BeanDefinition;
import lite.summer.service.v1.PetStoreService;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class BeanFactoryTest {


    @Test
    public void testGetBean(){
        BeanFactory factory = new DefaultBeanFactory("petstore-v1.xml");
        BeanDefinition definition = factory.getBeanDefinition("petStore");

        assertEquals("lite.summer.service.v1.PetStoreService", definition.getBeanClassName());

        PetStoreService petStoreService = (PetStoreService) factory.getBean("petStore");

        assertNotNull(petStoreService);
    }

    @Test
    public void testInvalidBean(){
        BeanFactory factory = new DefaultBeanFactory("petstore-v1.xml");
        try {
            factory.getBean("invalidBean");
        } catch (BeanCreationException e) {
            return;
        }
        Assert.fail("expect BeanCreationException ");
    }


}
