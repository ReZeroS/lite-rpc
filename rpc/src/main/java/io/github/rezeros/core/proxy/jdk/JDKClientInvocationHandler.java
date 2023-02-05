package io.github.rezeros.core.proxy.jdk;

import io.github.rezeros.core.concurrent.TimeoutInvocation;
import io.github.rezeros.protocol.RpcInvocation;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static io.github.rezeros.core.common.cache.CommonClientCache.RESP_MAP;
import static io.github.rezeros.core.common.cache.CommonClientCache.SEND_QUEUE;

public class JDKClientInvocationHandler implements InvocationHandler {

    private final static Object OBJECT = new Object();

    private Class<?> clazz;
    public JDKClientInvocationHandler(Class<?> targetClass) {
        clazz = targetClass;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        RpcInvocation rpcInvocation = new RpcInvocation();
        rpcInvocation.setArgs(args);
        rpcInvocation.setTargetMethod(method.getName());
        rpcInvocation.setTargetServiceName(clazz.getName());
        //这里面注入了一个uuid，对每一次的请求都做单独区分
        rpcInvocation.setUuid(UUID.randomUUID().toString());
        TimeoutInvocation timeoutInvocation = new TimeoutInvocation(null);
        RESP_MAP.put(rpcInvocation.getUuid(), timeoutInvocation);
        //这里就是将请求的参数放入到发送队列中
        SEND_QUEUE.add(rpcInvocation);
        if (timeoutInvocation.tryAcquire(3, TimeUnit.SECONDS)){
            return RESP_MAP.get(rpcInvocation.getUuid()).getRpcInvocation();
        }
        throw new TimeoutException("client wait server's response timeout!");
    }
}
