package lite.summer.core.type.classreading;

import lite.summer.core.io.Resource;
import lite.summer.core.type.AnnotationMetadata;
import lite.summer.core.type.ClassMetadata;
import org.objectweb.asm.ClassReader;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @Author: ReZero
 * @Date: 4/7/19 8:53 PM
 * @Version 1.0
 */
public class SimpleMetadataReader implements MetadataReader {
    private final Resource resource;

    private final ClassMetadata classMetadata;

    private final AnnotationMetadata annotationMetadata;


    public SimpleMetadataReader(Resource resource) throws IOException {
        InputStream is = new BufferedInputStream(resource.getInputStream());
        ClassReader classReader;

        try {
            classReader = new ClassReader(is);
        }
        finally {
            is.close();
        }

        AnnotationMetadataReadingVisitor visitor = new AnnotationMetadataReadingVisitor();
        classReader.accept(visitor, ClassReader.SKIP_DEBUG);

        this.annotationMetadata = visitor;
        this.classMetadata = visitor;
        this.resource = resource;
    }


    public Resource getResource() {
        return this.resource;
    }

    public ClassMetadata getClassMetadata() {
        return this.classMetadata;
    }

    public AnnotationMetadata getAnnotationMetadata() {
        return this.annotationMetadata;
    }

}
