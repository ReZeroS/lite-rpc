package io.github.rezeros.beans.factory.config;

import io.github.rezeros.core.MethodParameter;
import io.github.rezeros.util.Assert;

import java.io.Serializable;
import java.lang.reflect.Field;


/**
 * @Author: ReZero
 * @Date: 4/7/19 11:46 PM
 * @Version 1.0
 */
public class DependencyDescriptor implements Serializable {
    private transient Field field;
    private boolean required;
    private transient MethodParameter methodParameter;
    private Class declaringClass;
    private String methodName;
    private Class[] parameterTypes;
    private int parameterIndex;

    public DependencyDescriptor(Field field, boolean required) {
        Assert.notNull(field, "Field must not be null");
        this.field = field;
        this.required = required;

    }

    public DependencyDescriptor(boolean required, MethodParameter methodParameter) {
        Assert.notNull(methodParameter, "MethodParameter must not be null");
        this.methodParameter = methodParameter;
        this.declaringClass = methodParameter.getDeclaringClass();
        if (this.methodParameter.getMethod() != null) {
            this.methodName = methodParameter.getMethod().getName();
            this.parameterTypes = methodParameter.getMethod().getParameterTypes();
        }
        else {
            this.parameterTypes = methodParameter.getConstructor().getParameterTypes();
        }
        this.parameterIndex = methodParameter.getParameterIndex();
        this.required = required;
    }

    //
    public Class<?> getDependencyType(){
        return (this.field != null ? this.field.getType() : this.methodParameter.getParameterType());

    }

    public boolean isRequired() {
        return this.required;
    }

}
