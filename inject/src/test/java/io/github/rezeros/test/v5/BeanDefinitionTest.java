package io.github.rezeros.test.v5;

import io.github.rezeros.aop.aspectj.AspectJBeforeAdvice;
import io.github.rezeros.aop.aspectj.AspectJExpressionPointcut;
import io.github.rezeros.aop.config.AspectInstanceFactory;
import io.github.rezeros.aop.config.MethodLocatingFactory;
import io.github.rezeros.beans.BeanDefinition;
import io.github.rezeros.beans.PropertyValue;
import io.github.rezeros.beans.factory.config.RuntimeBeanReference;
import io.github.rezeros.beans.factory.support.DefaultBeanFactory;
import io.github.rezeros.tx.TransactionManager;
import io.github.rezeros.beans.ConstructorArgument.ValueHolder;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

/**
 * @Author: ReZero
 * @Date: 4/15/19 3:58 PM
 * @Version 1.0
 */
public class BeanDefinitionTest extends AbstractTestBaseNeeded {

    @Test
    public void testAOPBean() {

        DefaultBeanFactory factory = (DefaultBeanFactory) this.getBeanFactory("petstore-v5.xml");


        //检查名称为tx的Bean定义是否生成
        {
            BeanDefinition bd = factory.getBeanDefinition("tx");
            Assert.assertTrue(bd.getBeanClassName().equals(TransactionManager.class.getName()));
        }

        //检查placeOrder是否正确生成
        {
            BeanDefinition bd = factory.getBeanDefinition("placeOrder");
            //这个BeanDefinition是“合成的”
            Assert.assertTrue(bd.isSynthetic());
            Assert.assertTrue(bd.getBeanClass().equals(AspectJExpressionPointcut.class));


            PropertyValue pv = bd.getPropertyValues().get(0);
            Assert.assertEquals("expression", pv.getName());
            Assert.assertEquals("execution(* io.github.rezeros.service.v5.*.placeOrder(..))", pv.getValue());

        }


        //检查AspectJBeforeAdvice
        {
            String name = AspectJBeforeAdvice.class.getName() + "#0";
            BeanDefinition bd = factory.getBeanDefinition(name);
            Assert.assertTrue(bd.getBeanClass().equals(AspectJBeforeAdvice.class));

            //这个BeanDefinition是“合成的”
            Assert.assertTrue(bd.isSynthetic());

            List<ValueHolder> args = bd.getConstructorArgument().getArgumentValues();
            Assert.assertEquals(3, args.size());

            //构造函数第一个参数
            {
                BeanDefinition innerBeanDef = (BeanDefinition) args.get(0).getValue();
                Assert.assertTrue(innerBeanDef.isSynthetic());
                Assert.assertTrue(innerBeanDef.getBeanClass().equals(MethodLocatingFactory.class));

                List<PropertyValue> pvs = innerBeanDef.getPropertyValues();
                Assert.assertEquals("targetBeanName", pvs.get(0).getName());
                Assert.assertEquals("tx", pvs.get(0).getValue());
                Assert.assertEquals("methodName", pvs.get(1).getName());
                Assert.assertEquals("start", pvs.get(1).getValue());
            }

            //构造函数第二个参数
            {
                RuntimeBeanReference ref = (RuntimeBeanReference) args.get(1).getValue();
                Assert.assertEquals("placeOrder", ref.getBeanName());
            }

            //构造函数第三个参数
            {
                BeanDefinition innerBeanDef = (BeanDefinition) args.get(2).getValue();
                Assert.assertTrue(innerBeanDef.isSynthetic());
                Assert.assertTrue(innerBeanDef.getBeanClass().equals(AspectInstanceFactory.class));

                List<PropertyValue> pvs = innerBeanDef.getPropertyValues();
                Assert.assertEquals("aspectBeanName", pvs.get(0).getName());
                Assert.assertEquals("tx", pvs.get(0).getValue());

            }

        }



    }

}
