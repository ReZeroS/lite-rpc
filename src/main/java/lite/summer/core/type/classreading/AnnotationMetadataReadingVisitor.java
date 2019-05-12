package lite.summer.core.type.classreading;

import lite.summer.core.annotation.AnnotationAttributes;
import lite.summer.core.type.AnnotationMetadata;
import lite.summer.core.type.ClassMetadata;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Type;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/**
 * @Author: ReZero
 * @Date: 4/5/19 10:38 PM
 * @Version 1.0
 */
public class AnnotationMetadataReadingVisitor extends ClassMetadataReadingVisitor implements AnnotationMetadata {


    private final Set<String> annotationSet = new LinkedHashSet<>(4);
    private final Map<String, AnnotationAttributes> attributeMap = new LinkedHashMap<>(4);

    public AnnotationMetadataReadingVisitor() {
    }

    @Override
    public AnnotationVisitor visitAnnotation(final String descriptor, boolean visible) {
        String className = Type.getType(descriptor).getClassName();
        this.annotationSet.add(className);
        return new AnnotationAttributesReadingVisitor(className, this.attributeMap);
    }

    @Override
    public Set<String> getAnnotationTypes() {
        return this.annotationSet;
    }

    @Override
    public boolean hasAnnotation(String annotationType) {
        return this.annotationSet.contains(annotationType);
    }

    @Override
    public AnnotationAttributes getAnnotationAttributes(String annotationType) {
        return this.attributeMap.get(annotationType);
    }



}
