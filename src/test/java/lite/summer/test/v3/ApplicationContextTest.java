package lite.summer.test.v3;

import lite.summer.context.ApplicationContext;
import lite.summer.context.support.ClassPathXmlApplicationContext;

import lite.summer.dao.v3.ItemDao;
import lite.summer.dao.v3.AccountDao;
import lite.summer.service.v3.PetStoreService;
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
