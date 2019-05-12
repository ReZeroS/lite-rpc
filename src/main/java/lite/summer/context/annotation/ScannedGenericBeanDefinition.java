package lite.summer.context.annotation;

import lite.summer.beans.factory.annotation.AnnotatedBeanDefinition;
import lite.summer.beans.factory.support.GenericBeanDefinition;
import lite.summer.core.type.AnnotationMetadata;

import java.util.Objects;

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

        super.setBeanClassName(this.metadata.getClassName());
    }


    @Override
    public final AnnotationMetadata getMetadata() {
        return this.metadata;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ScannedGenericBeanDefinition)) {
            return false;
        }
        ScannedGenericBeanDefinition that = (ScannedGenericBeanDefinition) o;
        return Objects.equals(getMetadata(), that.getMetadata());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getMetadata());
    }
}
