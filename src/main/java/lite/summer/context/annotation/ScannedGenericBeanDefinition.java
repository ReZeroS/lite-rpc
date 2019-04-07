package lite.summer.context.annotation;

import lite.summer.beans.factory.annotation.AnnotatedBeanDefinition;
import lite.summer.beans.factory.support.GenericBeanDefinition;
import lite.summer.core.type.AnnotationMetadata;

/**
 * @Author: ReZero
 * @Date: 4/7/19 9:56 PM
 * @Version 1.0
 */
public class ScannedGenericBeanDefinition extends GenericBeanDefinition implements AnnotatedBeanDefinition {

    private final AnnotationMetadata metadata;


    public ScannedGenericBeanDefinition(AnnotationMetadata metadata) {
        super();

        this.metadata = metadata;

        setBeanClassName(this.metadata.getClassName());
    }


    public final AnnotationMetadata getMetadata() {
        return this.metadata;
    }

}
