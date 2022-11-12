package io.github.rezeros.test.v5;

import io.github.rezeros.aop.Advice;
import io.github.rezeros.aop.aspectj.AspectJAfterReturningAdvice;
import io.github.rezeros.aop.aspectj.AspectJAfterThrowingAdvice;
import io.github.rezeros.aop.aspectj.AspectJBeforeAdvice;
import io.github.rezeros.beans.factory.BeanFactory;
import io.github.rezeros.tx.TransactionManager;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

/**
 * @Author: ReZero
 * @Date: 4/15/19 4:50 PM
 * @Version 1.0
 */
public class BeanFactoryTest extends AbstractTestBaseNeeded {

    static String expectedExpression = "execution(* io.github.rezeros.service.v5.*.placeOrder(..))";
    @Test
    public void testGetBeanByType() throws Exception{

        BeanFactory factory = super.getBeanFactory("petstore-v5.xml");

        List<Object> advices = factory.getBeansByType(Advice.class);

        Assert.assertEquals(3, advices.size());

        {
            AspectJBeforeAdvice advice = (AspectJBeforeAdvice)this.getAdvice(AspectJBeforeAdvice.class, advices);

            Assert.assertEquals(TransactionManager.class.getMethod("start"), advice.getAdviceMethod());

            Assert.assertEquals(expectedExpression,advice.getPointcut().getExpression());

            Assert.assertEquals(TransactionManager.class,advice.getAdviceInstance().getClass());

        }


        {
            AspectJAfterReturningAdvice advice = (AspectJAfterReturningAdvice)this.getAdvice(AspectJAfterReturningAdvice.class, advices);

            Assert.assertEquals(TransactionManager.class.getMethod("commit"), advice.getAdviceMethod());

            Assert.assertEquals(expectedExpression,advice.getPointcut().getExpression());

            Assert.assertEquals(TransactionManager.class,advice.getAdviceInstance().getClass());

        }

        {
            AspectJAfterThrowingAdvice advice = (AspectJAfterThrowingAdvice)this.getAdvice(AspectJAfterThrowingAdvice.class, advices);

            Assert.assertEquals(TransactionManager.class.getMethod("rollback"), advice.getAdviceMethod());

            Assert.assertEquals(expectedExpression,advice.getPointcut().getExpression());

            Assert.assertEquals(TransactionManager.class,advice.getAdviceInstance().getClass());

        }


    }

    public Object getAdvice(Class<?> type,List<Object> advices){
        for(Object o : advices){
            if(o.getClass().equals(type)){
                return o;
            }
        }
        return null;
    }

}
