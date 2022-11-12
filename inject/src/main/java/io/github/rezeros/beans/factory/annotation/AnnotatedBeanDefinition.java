package io.github.rezeros.beans.factory.annotation;

import io.github.rezeros.core.type.AnnotationMetadata;
import io.github.rezeros.beans.BeanDefinition;

/**
 * @Author: ReZero
 * @Date: 4/7/19 9:57 PM
 * @Version 1.0
 */
public interface AnnotatedBeanDefinition extends BeanDefinition {
    AnnotationMetadata getMetadata();
}


