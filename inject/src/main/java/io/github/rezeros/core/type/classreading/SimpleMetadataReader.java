package io.github.rezeros.core.type.classreading;

import io.github.rezeros.core.io.Resource;
import io.github.rezeros.core.type.AnnotationMetadata;
import io.github.rezeros.core.type.ClassMetadata;
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
        } finally {
            is.close();
        }

        AnnotationMetadataReadingVisitor visitor = new AnnotationMetadataReadingVisitor();
        classReader.accept(visitor, ClassReader.SKIP_DEBUG);

        this.resource = resource;
        this.classMetadata = visitor;
        this.annotationMetadata = visitor;
    }


    @Override
    public Resource getResource() {
        return this.resource;
    }

    @Override
    public ClassMetadata getClassMetadata() {
        return this.classMetadata;
    }

    @Override
    public AnnotationMetadata getAnnotationMetadata() {
        return this.annotationMetadata;
    }

}
