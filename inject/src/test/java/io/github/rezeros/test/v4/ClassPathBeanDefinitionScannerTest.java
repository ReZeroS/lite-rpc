package io.github.rezeros.test.v4;

import io.github.rezeros.beans.BeanDefinition;
import io.github.rezeros.beans.factory.support.DefaultBeanFactory;
import io.github.rezeros.context.annotation.ClassPathBeanDefinitionScanner;
import io.github.rezeros.context.annotation.ScannedGenericBeanDefinition;
import io.github.rezeros.core.annotation.AnnotationAttributes;
import io.github.rezeros.core.type.AnnotationMetadata;
import io.github.rezeros.stereotype.Component;
import org.junit.Assert;
import org.junit.Test;

/**
 * @Author: ReZero
 * @Date: 4/7/19 9:49 PM
 * @Version 1.0
 */
public class ClassPathBeanDefinitionScannerTest {

    @Test
    public void testParseScanedBean(){

        DefaultBeanFactory factory = new DefaultBeanFactory();

        String basePackages = "io.github.rezeros.service.v4,io.github.rezeros.dao.v4";

        ClassPathBeanDefinitionScanner scanner = new ClassPathBeanDefinitionScanner(factory);
        scanner.doScan(basePackages);


        String annotation = Component.class.getName();

        {
            BeanDefinition bd = factory.getBeanDefinition("petStore");
            Assert.assertTrue(bd instanceof ScannedGenericBeanDefinition);
            ScannedGenericBeanDefinition sbd = (ScannedGenericBeanDefinition)bd;
            AnnotationMetadata annotationMetadata = sbd.getMetadata();


            Assert.assertTrue(annotationMetadata.hasAnnotation(annotation));
            AnnotationAttributes attributes = annotationMetadata.getAnnotationAttributes(annotation);
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
