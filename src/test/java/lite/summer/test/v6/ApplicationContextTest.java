package lite.summer.test.v6;

import lite.summer.context.ApplicationContext;
import lite.summer.context.support.ClassPathXmlApplicationContext;
import lite.summer.service.v6.IPetStoreService;
import lite.summer.util.MessageTracker;
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
