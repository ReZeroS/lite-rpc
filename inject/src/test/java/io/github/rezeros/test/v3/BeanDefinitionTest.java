package io.github.rezeros.test.v3;

import io.github.rezeros.beans.BeanDefinition;
import io.github.rezeros.beans.ConstructorArgument;
import io.github.rezeros.beans.factory.config.RuntimeBeanReference;
import io.github.rezeros.beans.factory.config.TypedStringValue;
import io.github.rezeros.beans.factory.support.DefaultBeanFactory;
import io.github.rezeros.beans.factory.xml.XmlBeanDefinitionReader;
import io.github.rezeros.core.io.ClassPathResource;
import io.github.rezeros.core.io.Resource;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

/**
 * @Author: ReZero
 * @Date: 4/1/19 5:55 PM
 * @Version 1.0
 */
public class BeanDefinitionTest {

    @Test
    public void testConstructorArgument() {

        DefaultBeanFactory factory = new DefaultBeanFactory();
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(factory);
        Resource resource = new ClassPathResource("petstore-v3.xml");
        reader.loadBeanDefinitions(resource);

        BeanDefinition bd = factory.getBeanDefinition("petStore");
        Assert.assertEquals("io.github.rezeros.service.v3.PetStoreService", bd.getBeanClassName());

        ConstructorArgument args = bd.getConstructorArgument();
        List<ConstructorArgument.ValueHolder> valueHolders = args.getArgumentValues();

        Assert.assertEquals(3, valueHolders.size());

        RuntimeBeanReference ref1 = (RuntimeBeanReference)valueHolders.get(0).getValue();
        Assert.assertEquals("accountDao", ref1.getBeanName());
        RuntimeBeanReference ref2 = (RuntimeBeanReference)valueHolders.get(1).getValue();
        Assert.assertEquals("itemDao", ref2.getBeanName());

        TypedStringValue strValue = (TypedStringValue)valueHolders.get(2).getValue();
        Assert.assertEquals( "1", strValue.getValue());
    }

}
