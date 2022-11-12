package io.github.rezeros.test.v4;

import io.github.rezeros.service.v4.PetStoreService;
import io.github.rezeros.context.ApplicationContext;
import io.github.rezeros.context.support.ClassPathXmlApplicationContext;
import org.junit.Assert;
import org.junit.Test;

/**
 * @Author: ReZero
 * @Date: 4/1/19 9:35 PM
 * @Version 1.0
 */
public class ApplicationContextTest {
    @Test
    public void testGetBeanProperty() {
        ApplicationContext ctx = new ClassPathXmlApplicationContext("petstore-v4.xml");
        PetStoreService petStore = (PetStoreService)ctx.getBean("petStore");

        Assert.assertNotNull(petStore.getAccountDao());
        Assert.assertNotNull(petStore.getItemDao());

    }
}
