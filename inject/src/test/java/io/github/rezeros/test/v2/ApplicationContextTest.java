package io.github.rezeros.test.v2;

import io.github.rezeros.context.ApplicationContext;
import io.github.rezeros.context.support.ClassPathXmlApplicationContext;
import io.github.rezeros.dao.v2.AccountDao;
import io.github.rezeros.dao.v2.ItemDao;
import io.github.rezeros.service.v2.PetStoreService;
import org.junit.Assert;
import org.junit.Test;

/**
 * @Author: ReZero
 * @Date: 3/19/19 9:46 PM
 * @Version 1.0
 */
public class ApplicationContextTest {
    @Test
    public void testGetBean(){
        ApplicationContext ctx = new ClassPathXmlApplicationContext("petstore-v2.xml");
        PetStoreService petStore = (PetStoreService) ctx.getBean("petStore");
        Assert.assertNotNull(petStore.getAccountDao());
        Assert.assertNotNull(petStore.getItemDao());

        Assert.assertTrue(petStore.getAccountDao() instanceof AccountDao);
        Assert.assertTrue(petStore.getItemDao() instanceof ItemDao);
        Assert.assertEquals("rezero", petStore.getOwner());
        Assert.assertEquals(2, petStore.getVersion());
    }


}
