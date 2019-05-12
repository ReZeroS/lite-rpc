package lite.summer.test.v4;

import lite.summer.context.ApplicationContext;
import lite.summer.context.support.ClassPathXmlApplicationContext;
import lite.summer.service.v4.PetStoreService;
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
