package lite.summer.core.type;

import lite.summer.core.annotation.AnnotationAttributes;

import java.util.Set;

/**
 * @Author: ReZero
 * @Date: 4/7/19 8:49 PM
 * @Version 1.0
 */
public interface AnnotationMetadata extends ClassMetadata {

    Set<String> getAnnotationTypes();


    boolean hasAnnotation(String annotationType);

    public AnnotationAttributes getAnnotationAttributes(String annotationType);

}
