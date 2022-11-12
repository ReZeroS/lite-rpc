package io.github.rezeros.beans.factory.annotation;

import io.github.rezeros.beans.factory.BeanCreationException;
import io.github.rezeros.beans.factory.config.AutowireCapableBeanFactory;
import io.github.rezeros.beans.factory.config.DependencyDescriptor;
import io.github.rezeros.util.ReflectionUtils;

import java.lang.reflect.Field;

/**
 * @Author: ReZero
 * @Date: 4/8/19 4:12 PM
 * @Version 1.0
 */
public class AutowiredFieldElement extends InjectionElement {
    boolean required;

    public AutowiredFieldElement(Field f, boolean required, AutowireCapableBeanFactory factory) {
        super(f,factory);
        this.required = required;
    }

    public Field getField(){
        return (Field)this.member;
    }
    @Override
    public void inject(Object target) {


        Field field = this.getField();
        try {

            DependencyDescriptor desc = new DependencyDescriptor(field, this.required);

            Object value = factory.resolveDependency(desc);

            if (value != null) {

                ReflectionUtils.makeAccessible(field);
                field.set(target, value);
            }
        }
        catch (Throwable ex) {
            throw new BeanCreationException("Could not autowire field: " + field, ex);
        }
    }

}
