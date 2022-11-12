package io.github.rezeros.core.type.classreading;

import io.github.rezeros.asm.SummerAsmInfo;
import io.github.rezeros.core.annotation.AnnotationAttributes;
import org.objectweb.asm.AnnotationVisitor;

import java.util.Map;

/**
 * @Author: ReZero
 * @Date: 4/7/19 8:02 PM
 * @Version 1.0
 */
final class AnnotationAttributesReadingVisitor extends AnnotationVisitor {

    private final String annotationType;

    private final Map<String, AnnotationAttributes> attributesMap;

    AnnotationAttributes attributes = new AnnotationAttributes();


    public AnnotationAttributesReadingVisitor(
            String annotationType, Map<String, AnnotationAttributes> attributesMap) {
        super(SummerAsmInfo.ASM_VERSION);

        this.annotationType = annotationType;
        this.attributesMap = attributesMap;

    }
    @Override
    public final void visitEnd(){
        this.attributesMap.put(this.annotationType, this.attributes);
    }

    @Override
    public void visit(String attributeName, Object attributeValue) {
        this.attributes.put(attributeName, attributeValue);
    }


}

