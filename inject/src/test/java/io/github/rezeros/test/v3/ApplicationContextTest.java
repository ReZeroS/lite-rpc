package io.github.rezeros.test.v3;

import io.github.rezeros.dao.v3.AccountDao;
import io.github.rezeros.dao.v3.ItemDao;
import io.github.rezeros.service.v3.PetStoreService;
import io.github.rezeros.context.ApplicationContext;
import io.github.rezeros.context.support.ClassPathXmlApplicationContext;

import org.junit.Assert;
import org.junit.Test;

/**
 * @Author: ReZero
 * @Date: 4/1/19 5:05 PM
 * @Version 1.0
 */
public class ApplicationContextTest {

    @Test
    public void testGetBean(){
        ApplicationContext ctx = new ClassPathXmlApplicationContext("petstore-v3.xml");
        PetStoreService petStore = (PetStoreService) ctx.getBean("petStore");
        Assert.assertNotNull(petStore.getAccountDao());
        Assert.assertNotNull(petStore.getItemDao());

        Assert.assertTrue(petStore.getAccountDao() instanceof AccountDao);
        Assert.assertTrue(petStore.getItemDao() instanceof ItemDao);
        Assert.assertEquals(1, petStore.getVersion());
    }


}
