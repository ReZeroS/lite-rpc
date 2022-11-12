package io.github.rezeros.core.type.classreading;

import io.github.rezeros.core.io.Resource;
import io.github.rezeros.core.type.AnnotationMetadata;
import io.github.rezeros.core.type.ClassMetadata;

/**
 * @Author: ReZero
 * @Date: 4/7/19 8:45 PM
 * @Version 1.0
 *
 * Simple facade for accessing class metadata,
 * as read by an ASM {@link org.objectweb.asm.ClassReader}.
 *
 */
public interface MetadataReader {

    /**
     * Return the resource reference for the class file.
     */
    Resource getResource();

    /**
     * Read basic class metadata for the underlying class.
     */
    ClassMetadata getClassMetadata();

    /**
     * Read full annotation metadata for the underlying class,
     * including metadata for annotated methods.
     */
    AnnotationMetadata getAnnotationMetadata();

}

