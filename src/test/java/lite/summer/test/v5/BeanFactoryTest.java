package lite.summer.test.v5;

import lite.summer.aop.Advice;
import lite.summer.aop.aspectj.AspectJAfterReturningAdvice;
import lite.summer.aop.aspectj.AspectJAfterThrowingAdvice;
import lite.summer.aop.aspectj.AspectJBeforeAdvice;
import lite.summer.beans.factory.BeanFactory;
import lite.summer.tx.TransactionManager;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

/**
 * @Author: ReZero
 * @Date: 4/15/19 4:50 PM
 * @Version 1.0
 */
public class BeanFactoryTest extends AbstractTestBaseNeeded {

    static String expectedExpression = "execution(* lite.summer.service.v5.*.placeOrder(..))";
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
