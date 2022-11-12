package io.github.rezeros.test.v6;

import io.github.rezeros.context.ApplicationContext;
import io.github.rezeros.context.support.ClassPathXmlApplicationContext;
import io.github.rezeros.service.v6.IPetStoreService;
import io.github.rezeros.util.MessageTracker;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class ApplicationContextTest {
	
	
	
	@Test
	public void testGetBeanProperty() {
		
		ApplicationContext ctx = new ClassPathXmlApplicationContext("petstore-v6.xml");
		IPetStoreService petStore = (IPetStoreService)ctx.getBean("petStore");
	
		petStore.placeOrder();
		
		List<String> msgs = MessageTracker.getMsgs();
		
		Assert.assertEquals(3, msgs.size());
		Assert.assertEquals("start tx", msgs.get(0));	
		Assert.assertEquals("place order", msgs.get(1));	
		Assert.assertEquals("commit tx", msgs.get(2));	
		
	}

	@Before
	public void setUp(){
		MessageTracker.clearMsgs();
	}
	
	
}
