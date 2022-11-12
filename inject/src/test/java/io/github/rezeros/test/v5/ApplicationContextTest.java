package io.github.rezeros.test.v5;

import io.github.rezeros.service.v5.PetStoreService;
import io.github.rezeros.context.ApplicationContext;
import io.github.rezeros.context.support.ClassPathXmlApplicationContext;
import io.github.rezeros.util.MessageTracker;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

/**
 * @Author: ReZero
 * @Date: 4/8/19 8:34 PM
 * @Version 1.0
 */
public class ApplicationContextTest {
    @Before
    public void setUp(){
        MessageTracker.clearMsgs();
    }
    @Test
    public void testPlaceOrder() {

        ApplicationContext ctx = new ClassPathXmlApplicationContext("petstore-v5.xml");
        PetStoreService petStore = (PetStoreService)ctx.getBean("petStore");

        Assert.assertNotNull(petStore.getAccountDao());
        Assert.assertNotNull(petStore.getItemDao());

        petStore.placeOrder();

        List<String> msgs = MessageTracker.getMsgs();

        Assert.assertEquals(3, msgs.size());
        Assert.assertEquals("start tx", msgs.get(0));
        Assert.assertEquals("place order", msgs.get(1));
        Assert.assertEquals("commit tx", msgs.get(2));

    }


}
