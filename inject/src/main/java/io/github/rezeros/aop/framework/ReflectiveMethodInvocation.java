package io.github.rezeros.aop.framework;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Method;
import java.util.List;

/**
 * @Author: ReZero
 * @Date: 4/9/19 3:09 PM
 * @Version 1.0
 */
public class ReflectiveMethodInvocation implements MethodInvocation {


    protected final Object targetObject;

    protected final Method targetMethod;

    protected Object[] arguments;


    /**
     * List of MethodInterceptor
     */
    protected final List<MethodInterceptor> interceptors;

    /**
     * Index from 0 of the current interceptor we're invoking.
     * -1 until we invoke: then the current interceptor.
     */
    private int currentInterceptorIndex = -1;


    public ReflectiveMethodInvocation(
            Object target, Method method, Object[] arguments,
            List<MethodInterceptor> interceptors) {

        this.targetObject = target;
        this.targetMethod = method;
        this.arguments = arguments;
        this.interceptors = interceptors;
    }


    @Override
    public final Object getThis() {
        return this.targetObject;
    }


    /**
     * Return the method invoked on the proxied interface.
     * May or may not correspond with a method invoked on an underlying
     * implementation of that interface.
     */
    @Override
    public final Method getMethod() {
        return this.targetMethod;
    }

    @Override
    public final Object[] getArguments() {
        return (this.arguments != null ? this.arguments : new Object[0]);
    }


    @Override
    public Object proceed() throws Throwable {
        //	所有的拦截器已经调用完成
        if (this.currentInterceptorIndex == this.interceptors.size() - 1) {
            return invokeJoinpoint();
        }

        this.currentInterceptorIndex++;

        MethodInterceptor interceptor =
                this.interceptors.get(this.currentInterceptorIndex);

        return interceptor.invoke(this);

    }

    /**
     * Invoke the joinpoint using reflection.
     * Subclasses can override this to use custom invocation.
     *
     * @return the return value of the joinpoint
     * @throws Throwable if invoking the joinpoint resulted in an exception
     */
    protected Object invokeJoinpoint() throws Throwable {
        return this.targetMethod.invoke(this.targetObject, this.arguments);
    }

    @Override
    public AccessibleObject getStaticPart() {
        return this.targetMethod;
    }

}


