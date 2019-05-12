package lite.summer.test.v5;

import lite.summer.aop.aspectj.AspectJAfterReturningAdvice;
import lite.summer.aop.aspectj.AspectJBeforeAdvice;
import lite.summer.aop.aspectj.AspectJExpressionPointcut;
import lite.summer.aop.config.AspectInstanceFactory;
import lite.summer.aop.framework.AopConfig;
import lite.summer.aop.framework.AopConfigSupport;
import lite.summer.aop.framework.CglibProxyFactory;
import lite.summer.beans.factory.BeanFactory;
import lite.summer.service.v5.PetStoreService;
import lite.summer.util.MessageTracker;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

/**
 * @Author: ReZero
 * @Date: 4/9/19 8:25 PM
 * @Version 1.0
 */
public class CGLibAopProxyTest extends AbstractTestBaseNeeded {

    private AspectJBeforeAdvice beforeAdvice = null;
    private AspectJAfterReturningAdvice afterAdvice = null;
    private AspectJExpressionPointcut pc = null;
    private BeanFactory beanFactory = null;
    private AspectInstanceFactory aspectInstanceFactory = null;

    @Before
    public void setUp() throws Exception {

        MessageTracker.clearMsgs();

        String expression = "execution(* lite.summer.service.v5.*.placeOrder(..))";
        pc = new AspectJExpressionPointcut();
        pc.setExpression(expression);

        beanFactory = this.getBeanFactory("petstore-v5.xml");
        aspectInstanceFactory = this.getAspectInstanceFactory("tx");
        aspectInstanceFactory.setBeanFactory(beanFactory);

        beforeAdvice = new AspectJBeforeAdvice(
                getAdviceMethod("start"),
                pc,
                aspectInstanceFactory);

        afterAdvice = new AspectJAfterReturningAdvice(
                getAdviceMethod("commit"),
                pc,
                aspectInstanceFactory);

    }

    @Test
    public void testGetProxy() {

        AopConfig config = new AopConfigSupport();

        config.addAdvice(beforeAdvice);
        config.addAdvice(afterAdvice);
        config.setTargetObject(new PetStoreService());


        CglibProxyFactory proxyFactory = new CglibProxyFactory(config);

        PetStoreService proxy = (PetStoreService) proxyFactory.getProxy();

        proxy.placeOrder();


        List<String> msgs = MessageTracker.getMsgs();
        Assert.assertEquals(3, msgs.size());
        Assert.assertEquals("start tx", msgs.get(0));
        Assert.assertEquals("place order", msgs.get(1));
        Assert.assertEquals("commit tx", msgs.get(2));

        proxy.toString();
    }


}
