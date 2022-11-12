package io.github.rezeros.test.v4;


import io.github.rezeros.core.annotation.AnnotationAttributes;
import io.github.rezeros.core.io.ClassPathResource;
import io.github.rezeros.core.type.AnnotationMetadata;
import io.github.rezeros.core.type.classreading.MetadataReader;
import io.github.rezeros.core.type.classreading.SimpleMetadataReader;
import io.github.rezeros.stereotype.Component;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

/**
 * @Author: ReZero
 * @Date: 4/7/19 8:38 PM
 * @Version 1.0
 */
public class MetadataReaderTest {

    @Test
    public void testGetMetadata() throws IOException {
        ClassPathResource resource = new ClassPathResource("io/github/rezeros/service/v4/PetStoreService.class");

        MetadataReader metadataReader = new SimpleMetadataReader(resource);
        //注意：不需要单独使用ClassMetadata
        //ClassMetadata clzMetadata = reader.getClassMetadata();
        AnnotationMetadata annotationMetadata = metadataReader.getAnnotationMetadata();

        String annotation = Component.class.getName();

        Assert.assertTrue(annotationMetadata.hasAnnotation(annotation));
        AnnotationAttributes attributes = annotationMetadata.getAnnotationAttributes(annotation);
        Assert.assertEquals("petStore", attributes.get("value"));

        //注：下面对class metadata的测试并不充分
        Assert.assertFalse(annotationMetadata.isAbstract());
        Assert.assertFalse(annotationMetadata.isFinal());
        Assert.assertEquals("io.github.rezeros.service.v4.PetStoreService", annotationMetadata.getClassName());

    }

}
