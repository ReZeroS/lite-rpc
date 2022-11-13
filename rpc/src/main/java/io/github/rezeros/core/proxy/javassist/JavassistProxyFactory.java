package io.github.rezeros.core.proxy.javassist;

import io.github.rezeros.core.proxy.ProxyFactory;

import java.lang.reflect.Proxy;

public class JavassistProxyFactory implements ProxyFactory {

    @Override
    public <T> T getProxy(Class clazz) throws Throwable {
        return (T) ProxyGenerator.newProxyInstance(Thread.currentThread().getContextClassLoader(),
                clazz, new JavassistInvocationHandler(clazz));
    }
}