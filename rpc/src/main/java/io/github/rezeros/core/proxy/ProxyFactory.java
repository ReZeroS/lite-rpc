package io.github.rezeros.core.proxy;

import io.github.rezeros.core.client.RpcReferenceWrapper;

public interface ProxyFactory {
    <T> T getProxy(RpcReferenceWrapper rpcReferenceWrapper) throws Throwable;

}
