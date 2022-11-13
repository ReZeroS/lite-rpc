package io.github.rezeros.core.proxy;

public interface ProxyFactory {

    <T> T getProxy(final Class clazz) throws Throwable;
}
