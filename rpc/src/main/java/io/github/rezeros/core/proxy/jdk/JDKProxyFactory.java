package io.github.rezeros.core.proxy.jdk;


import io.github.rezeros.core.client.RpcReferenceWrapper;
import io.github.rezeros.core.proxy.ProxyFactory;

import java.lang.reflect.Proxy;

public class JDKProxyFactory implements ProxyFactory {
    public <T> T getProxy(RpcReferenceWrapper rpcReferenceWrapper) {
//        return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz}, new JDKClientInvocationHandler(clazz));
        return null;
    }

}
