package io.github.rezeros.test.v4;

import io.github.rezeros.beans.BeanDefinition;
import io.github.rezeros.beans.factory.support.DefaultBeanFactory;
import io.github.rezeros.beans.factory.xml.XmlBeanDefinitionReader;
import io.github.rezeros.context.annotation.ScannedGenericBeanDefinition;
import io.github.rezeros.core.annotation.AnnotationAttributes;
import io.github.rezeros.core.io.ClassPathResource;
import io.github.rezeros.core.io.Resource;
import io.github.rezeros.core.type.AnnotationMetadata;
import io.github.rezeros.stereotype.Component;
import org.junit.Assert;
import org.junit.Test;

/**
 * @Author: ReZero
 * @Date: 4/7/19 10:40 PM
 * @Version 1.0
 */
public class XmlBeanDefinitionReaderTest {

    @Test
    public void testParseScanedBean(){

        DefaultBeanFactory factory = new DefaultBeanFactory();
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(factory);
        Resource resource = new ClassPathResource("petstore-v4.xml");
        reader.loadBeanDefinitions(resource);
        String annotation = Component.class.getName();

        //下面的代码和ClassPathBeanDefinitionScannerTest重复，该怎么处理？
        {
            BeanDefinition bd = factory.getBeanDefinition("petStore");
            Assert.assertTrue(bd instanceof ScannedGenericBeanDefinition);
            ScannedGenericBeanDefinition sbd = (ScannedGenericBeanDefinition)bd;
            AnnotationMetadata amd = sbd.getMetadata();


            Assert.assertTrue(amd.hasAnnotation(annotation));
            AnnotationAttributes attributes = amd.getAnnotationAttributes(annotation);
            Assert.assertEquals("petStore", attributes.get("value"));
        }
        {
            BeanDefinition bd = factory.getBeanDefinition("accountDao");
            Assert.assertTrue(bd instanceof ScannedGenericBeanDefinition);
            ScannedGenericBeanDefinition sbd = (ScannedGenericBeanDefinition)bd;
            AnnotationMetadata amd = sbd.getMetadata();
            Assert.assertTrue(amd.hasAnnotation(annotation));
        }
        {
            BeanDefinition bd = factory.getBeanDefinition("itemDao");
            Assert.assertTrue(bd instanceof ScannedGenericBeanDefinition);
            ScannedGenericBeanDefinition sbd = (ScannedGenericBeanDefinition)bd;
            AnnotationMetadata amd = sbd.getMetadata();
            Assert.assertTrue(amd.hasAnnotation(annotation));
        }
    }


}
