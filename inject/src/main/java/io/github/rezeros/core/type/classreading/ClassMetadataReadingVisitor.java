package io.github.rezeros.core.type.classreading;

import io.github.rezeros.asm.SummerAsmInfo;
import io.github.rezeros.core.type.ClassMetadata;
import io.github.rezeros.util.ClassUtils;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Opcodes;


/**
 * @Author: ReZero
 * @Date: 4/5/19 10:24 PM
 * @Version 1.0
 */
public class ClassMetadataReadingVisitor extends ClassVisitor implements ClassMetadata {

    private String className;

    private boolean isInterface;

    private boolean isAbstract;

    private boolean isFinal;

    private String superClassName;

    private String[] interfaces;


    public ClassMetadataReadingVisitor() {
        super(SummerAsmInfo.ASM_VERSION);
    }


    @Override
    public void visit(int version, int access, String name, String signature, String supername, String[] interfaces) {
        this.className = ClassUtils.convertResourcePathToClassName(name);
        this.isInterface = ((access & Opcodes.ACC_INTERFACE) != 0);
        this.isAbstract = ((access & Opcodes.ACC_ABSTRACT) != 0);
        this.isFinal = ((access & Opcodes.ACC_FINAL) != 0);
        if (supername != null) {
            this.superClassName = ClassUtils.convertResourcePathToClassName(supername);
        }
        this.interfaces = new String[interfaces.length];
        for (int i = 0; i < interfaces.length; i++) {
            this.interfaces[i] = ClassUtils.convertResourcePathToClassName(interfaces[i]);
        }
    }


    @Override
    public String getClassName() {
        return this.className;
    }

    @Override
    public boolean isInterface() {
        return this.isInterface;
    }

    @Override
    public boolean isAbstract() {
        return this.isAbstract;
    }

    public boolean isConcrete() {
        return !(this.isInterface || this.isAbstract);
    }

    @Override
    public boolean isFinal() {
        return this.isFinal;
    }


    @Override
    public boolean hasSuperClass() {
        return (this.superClassName != null);
    }

    @Override
    public String getSuperClassName() {
        return this.superClassName;
    }

    @Override
    public String[] getInterfaceNames() {
        return this.interfaces;
    }

}
