package lite.summer.test.v4;

import lite.summer.core.annotation.AnnotationAttributes;
import lite.summer.core.io.ClassPathResource;
import lite.summer.core.type.classreading.AnnotationMetadataReadingVisitor;
import lite.summer.core.type.classreading.ClassMetadataReadingVisitor;
import org.junit.Assert;
import org.junit.Test;
import org.objectweb.asm.ClassReader;


import java.io.IOException;

/**
 * @Author: ReZero
 * @Date: 4/2/19 10:56 PM
 * @Version 1.0
 */
public class ClassReaderTest {


    @Test
    public void testGetClasMetaData() throws IOException {
        ClassPathResource resource = new ClassPathResource("lite/summer/service/v4/PetStoreService.class");
        ClassReader reader = new ClassReader(resource.getInputStream());

        ClassMetadataReadingVisitor visitor = new ClassMetadataReadingVisitor();

        reader.accept(visitor, ClassReader.SKIP_DEBUG);

        Assert.assertFalse(visitor.isAbstract());
        Assert.assertFalse(visitor.isInterface());
        Assert.assertFalse(visitor.isFinal());
        Assert.assertEquals("lite.summer.service.v4.PetStoreService", visitor.getClassName());
        Assert.assertEquals("java.lang.Object", visitor.getSuperClassName());
        Assert.assertEquals(0, visitor.getInterfaceNames().length);
    }


    @Test
    public void testGetAnnonation() throws Exception{
        ClassPathResource resource = new ClassPathResource("lite/summer/service/v4/PetStoreService.class");
        ClassReader reader = new ClassReader(resource.getInputStream());

        AnnotationMetadataReadingVisitor visitor = new AnnotationMetadataReadingVisitor();

        reader.accept(visitor, ClassReader.SKIP_DEBUG);

        String annotation = "lite.summer.stereotype.Component";
        Assert.assertTrue(visitor.hasAnnotation(annotation));

        AnnotationAttributes attributes = visitor.getAnnotationAttributes(annotation);

        Assert.assertEquals("petStore", attributes.get("value"));

    }










}
