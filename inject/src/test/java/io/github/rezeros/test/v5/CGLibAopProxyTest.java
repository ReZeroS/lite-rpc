package io.github.rezeros.test.v5;

import io.github.rezeros.service.v5.PetStoreService;
import io.github.rezeros.aop.aspectj.AspectJAfterReturningAdvice;
import io.github.rezeros.aop.aspectj.AspectJBeforeAdvice;
import io.github.rezeros.aop.aspectj.AspectJExpressionPointcut;
import io.github.rezeros.aop.config.AspectInstanceFactory;
import io.github.rezeros.aop.framework.AopConfig;
import io.github.rezeros.aop.framework.AopConfigSupport;
import io.github.rezeros.aop.framework.CglibProxyFactory;
import io.github.rezeros.beans.factory.BeanFactory;
import io.github.rezeros.util.MessageTracker;
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

        String expression = "execution(* io.github.rezeros.service.v5.*.placeOrder(..))";
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
