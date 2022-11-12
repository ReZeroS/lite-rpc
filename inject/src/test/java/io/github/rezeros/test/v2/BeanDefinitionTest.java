package io.github.rezeros.test.v2;

import io.github.rezeros.beans.BeanDefinition;
import io.github.rezeros.beans.PropertyValue;
import io.github.rezeros.beans.factory.config.RuntimeBeanReference;
import io.github.rezeros.beans.factory.support.DefaultBeanFactory;
import io.github.rezeros.beans.factory.xml.XmlBeanDefinitionReader;
import io.github.rezeros.core.io.ClassPathResource;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

/**
 * @Author: ReZero
 * @Date: 3/19/19 10:15 PM
 * @Version 1.0
 */
public class BeanDefinitionTest {

    @Test
    public void testGetBeanDefinition(){
        DefaultBeanFactory factory = new DefaultBeanFactory();

        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(factory);

        reader.loadBeanDefinitions(new ClassPathResource("petstore-v2.xml"));

        BeanDefinition beanDefinition = factory.getBeanDefinition("petStore");
        List<PropertyValue> propertyValueList = beanDefinition.getPropertyValues();
        Assert.assertTrue(propertyValueList.size() == 5);
        {
            PropertyValue pv = this.getPropertyValue("accountDao", propertyValueList);
            Assert.assertNotNull(pv);
            Assert.assertTrue(pv.getValue() instanceof RuntimeBeanReference);
        }

        {
            PropertyValue pv = this.getPropertyValue("itemDao", propertyValueList);
            Assert.assertNotNull(pv);
            Assert.assertTrue(pv.getValue() instanceof RuntimeBeanReference);
        }


    }

    private PropertyValue getPropertyValue(String name, List<PropertyValue> propertyValueList) {
        for (PropertyValue propertyValue : propertyValueList) {
            if (propertyValue.getName().equals(name)){
                return propertyValue;
            }
        }
        return null;
    }

}
