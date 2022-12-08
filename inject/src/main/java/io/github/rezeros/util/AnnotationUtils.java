package io.github.rezeros.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.Arrays;

/**
 * @Author: ReZero
 * @Date: 4/8/19 5:47 PM
 * @Version 1.0
 */
public class AnnotationUtils {

    public static <T extends Annotation> T getAnnotation(AnnotatedElement annotatedElement, Class<T> annotationType) {
        T annotation = annotatedElement.getAnnotation(annotationType);
        if (annotation == null) {
            for (Annotation metaAnn : annotatedElement.getAnnotations()) {
                annotation = metaAnn.annotationType().getAnnotation(annotationType);
                if (annotation != null) {
                    break;
                }
            }
        }
        return annotation;
    }

    public static boolean hasAnnotation(Class<?> interfaceClass, Class<? extends Annotation> annotation){
        return interfaceClass.isAnnotationPresent(annotation);
    }


}
