package io.github.rezeros.test.v5;

import io.github.rezeros.service.v5.PetStoreService;
import io.github.rezeros.aop.aspectj.AspectJAfterReturningAdvice;
import io.github.rezeros.aop.aspectj.AspectJAfterThrowingAdvice;
import io.github.rezeros.aop.aspectj.AspectJBeforeAdvice;
import io.github.rezeros.aop.aspectj.AspectJExpressionPointcut;
import io.github.rezeros.aop.config.AspectInstanceFactory;
import io.github.rezeros.aop.framework.ReflectiveMethodInvocation;
import io.github.rezeros.beans.factory.BeanFactory;
import io.github.rezeros.tx.TransactionManager;
import io.github.rezeros.util.MessageTracker;
import org.aopalliance.intercept.MethodInterceptor;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: ReZero
 * @Date: 4/9/19 1:41 PM
 * @Version 1.0
 */
public class ReflectiveMethodInvocationTest extends AbstractTestBaseNeeded {


    private AspectJBeforeAdvice beforeAdvice = null;
    private AspectJAfterReturningAdvice afterAdvice = null;
    private AspectJExpressionPointcut pc = null;
    private BeanFactory beanFactory = null;
    private AspectInstanceFactory aspectInstanceFactory = null;

    private AspectJAfterThrowingAdvice afterThrowingAdvice = null;
    private PetStoreService petStoreService = null;
    private TransactionManager tx;


    @Before
    public void setUp() throws Exception {
        petStoreService = new PetStoreService();
        tx = new TransactionManager();

        MessageTracker.clearMsgs();

        beanFactory = this.getBeanFactory("petstore-v5.xml");
        aspectInstanceFactory = this.getAspectInstanceFactory("tx");
        aspectInstanceFactory.setBeanFactory(beanFactory);

        beforeAdvice = new AspectJBeforeAdvice(
                this.getAdviceMethod("start"),
                null,
                aspectInstanceFactory);

        afterAdvice = new AspectJAfterReturningAdvice(
                this.getAdviceMethod("commit"),
                null,
                aspectInstanceFactory);

        afterThrowingAdvice = new AspectJAfterThrowingAdvice(
                this.getAdviceMethod("rollback"),
                null,
                aspectInstanceFactory
        );

    }


    @Test
    public void testMethodInvocation() throws Throwable {


        Method targetMethod = PetStoreService.class.getMethod("placeOrder");

        List<MethodInterceptor> interceptors = new ArrayList<MethodInterceptor>();
        interceptors.add(beforeAdvice);
        interceptors.add(afterAdvice);


        ReflectiveMethodInvocation mi = new ReflectiveMethodInvocation(petStoreService, targetMethod, new Object[0], interceptors);

        mi.proceed();


        List<String> msgs = MessageTracker.getMsgs();
        Assert.assertEquals(3, msgs.size());
        Assert.assertEquals("start tx", msgs.get(0));
        Assert.assertEquals("place order", msgs.get(1));
        Assert.assertEquals("commit tx", msgs.get(2));

    }

    @Test
    public void testMethodInvocation2() throws Throwable {


        Method targetMethod = PetStoreService.class.getMethod("placeOrder");

        List<MethodInterceptor> interceptors = new ArrayList<MethodInterceptor>();
        interceptors.add(afterAdvice);
        interceptors.add(beforeAdvice);


        ReflectiveMethodInvocation mi = new ReflectiveMethodInvocation(petStoreService, targetMethod, new Object[0], interceptors);

        mi.proceed();


        List<String> msgs = MessageTracker.getMsgs();
        Assert.assertEquals(3, msgs.size());
        Assert.assertEquals("start tx", msgs.get(0));
        Assert.assertEquals("place order", msgs.get(1));
        Assert.assertEquals("commit tx", msgs.get(2));

    }

    @Test
    public void testAfterThrowing() throws Throwable {


        Method targetMethod = PetStoreService.class.getMethod("placeOrderWithException");

        List<MethodInterceptor> interceptors = new ArrayList<MethodInterceptor>();
        interceptors.add(afterThrowingAdvice);
        interceptors.add(beforeAdvice);


        ReflectiveMethodInvocation mi = new ReflectiveMethodInvocation(petStoreService, targetMethod, new Object[0], interceptors);
        try {
            mi.proceed();

        } catch (Throwable t) {
            List<String> msgs = MessageTracker.getMsgs();
            Assert.assertEquals(2, msgs.size());
            Assert.assertEquals("start tx", msgs.get(0));
            Assert.assertEquals("rollback tx", msgs.get(1));
            return;
        }


        Assert.fail("No Exception thrown");


    }
}
