package io.github.rezeros.test.v1;

import io.github.rezeros.context.ApplicationContext;
import io.github.rezeros.context.support.ClassPathXmlApplicationContext;
import io.github.rezeros.context.support.FileSystemXmlApplicationContext;
import io.github.rezeros.service.v1.PetStoreService;
import org.junit.Assert;
import org.junit.Test;

public class ApplicationContextTest {

    @Test
    public void testGetBean(){
        ApplicationContext ctx = new ClassPathXmlApplicationContext("petstore-v1.xml");
        PetStoreService petStore = (PetStoreService) ctx.getBean("petStore");
        Assert.assertNotNull(petStore);
    }

    @Test
    public void testGetBeanFrom(){
        ApplicationContext ctx = new FileSystemXmlApplicationContext("src/test/resources/petstore-v1.xml");
        PetStoreService petStoreService = (PetStoreService) ctx.getBean("petStore");
        Assert.assertNotNull(petStoreService);
    }


}
