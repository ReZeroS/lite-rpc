package lite.summer.test.v4;

import lite.summer.beans.BeanDefinition;
import lite.summer.beans.factory.support.DefaultBeanFactory;
import lite.summer.context.annotation.ClassPathBeanDefinitionScanner;
import lite.summer.context.annotation.ScannedGenericBeanDefinition;
import lite.summer.core.annotation.AnnotationAttributes;
import lite.summer.core.type.AnnotationMetadata;
import lite.summer.stereotype.Component;
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

        String basePackages = "lite.summer.service.v4,org.litespring.dao.v4";

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
