package lite.summer.test.v4;


import lite.summer.core.annotation.AnnotationAttributes;
import lite.summer.core.io.ClassPathResource;
import lite.summer.core.type.AnnotationMetadata;
import lite.summer.core.type.classreading.MetadataReader;
import lite.summer.core.type.classreading.SimpleMetadataReader;
import lite.summer.stereotype.Component;
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
        ClassPathResource resource = new ClassPathResource("lite/summer/service/v4/PetStoreService.class");

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
        Assert.assertEquals("lite.summer.service.v4.PetStoreService", annotationMetadata.getClassName());

    }

}
