package lite.summer.beans.factory.annotation;

import lite.summer.beans.BeanDefinition;
import lite.summer.core.type.AnnotationMetadata;

/**
 * @Author: ReZero
 * @Date: 4/7/19 9:57 PM
 * @Version 1.0
 */
public interface AnnotatedBeanDefinition extends BeanDefinition {
    AnnotationMetadata getMetadata();
}


