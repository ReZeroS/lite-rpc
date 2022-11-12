package io.github.rezeros.core;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

/**
 * @Author: ReZero
 * @Date: 5/12/19 9:39 AM
 * @Version 1.0
 */
public class MethodParameter {

    private Method method;

    private Constructor constructor;

    private Class<?> parameterType;

    private String parameterName;

    private final int parameterIndex;

    /**
     * Create a new MethodParameter for the given method, with nesting level 1.
     * @param method the Method to specify a parameter for
     * @param parameterIndex the index of the parameter
     */
    public MethodParameter(Method method, int parameterIndex) {
        this.method = method;
        this.parameterIndex = parameterIndex;
    }



    /**
     * Create a new MethodParameter for the given constructor, with nesting level 1.
     * @param constructor the Constructor to specify a parameter for
     * @param parameterIndex the index of the parameter
     */
    public MethodParameter(Constructor constructor, int parameterIndex) {
        this.constructor = constructor;
        this.parameterIndex = parameterIndex;
    }




    /**
     * Return the wrapped Method, if any.
     * <p>Note: Either Method or Constructor is available.
     * @return the Method, or <code>null</code> if none
     */
    public Method getMethod() {
        return this.method;
    }

    /**
     * Return the wrapped Constructor, if any.
     * <p>Note: Either Method or Constructor is available.
     * @return the Constructor, or <code>null</code> if none
     */
    public Constructor getConstructor() {
        return this.constructor;
    }

    /**
     * Return the class that declares the underlying Method or Constructor.
     */
    public Class getDeclaringClass() {
        return (this.method != null ? this.method.getDeclaringClass() : this.constructor.getDeclaringClass());
    }



    /**
     * Set a resolved (generic) parameter type.
     */
    void setParameterType(Class<?> parameterType) {
        this.parameterType = parameterType;
    }


    /**
     * Return the annotations associated with the target method/constructor itself.
     */
    public Annotation[] getMethodAnnotations() {
        return (this.method != null ? this.method.getAnnotations() : this.constructor.getAnnotations());
    }

    /**
     * Return the method/constructor annotation of the given type, if available.
     * @param annotationType the annotation type to look for
     * @return the annotation object, or <code>null</code> if not found
     */
    @SuppressWarnings("unchecked")
    public <T extends Annotation> T getMethodAnnotation(Class<T> annotationType) {
        return (this.method != null ? this.method.getAnnotation(annotationType) :
                (T) this.constructor.getAnnotation(annotationType));
    }

    /**
     * Create a new MethodParameter for the given method or constructor.
     * <p>This is a convenience constructor for scenarios where a
     * Method or Constructor reference is treated in a generic fashion.
     * @param methodOrConstructor the Method or Constructor to specify a parameter for
     * @param parameterIndex the index of the parameter
     * @return the corresponding MethodParameter instance
     */
    public static MethodParameter forMethodOrConstructor(Object methodOrConstructor, int parameterIndex) {
        if (methodOrConstructor instanceof Method) {
            return new MethodParameter((Method) methodOrConstructor, parameterIndex);
        }
        else if (methodOrConstructor instanceof Constructor) {
            return new MethodParameter((Constructor) methodOrConstructor, parameterIndex);
        }
        else {
            throw new IllegalArgumentException(
                    "Given object [" + methodOrConstructor + "] is neither a Method nor a Constructor");
        }
    }

    public int getParameterIndex() {
        return parameterIndex;
    }

    public Class<?> getParameterType() {
        if (this.parameterType == null) {
            if (this.parameterIndex < 0) {
                this.parameterType = (this.method != null ? this.method.getReturnType() : null);
            }
            else {
                this.parameterType = (this.method != null ?
                        this.method.getParameterTypes()[this.parameterIndex] :
                        this.constructor.getParameterTypes()[this.parameterIndex]);
            }
        }
        return this.parameterType;
    }
}
