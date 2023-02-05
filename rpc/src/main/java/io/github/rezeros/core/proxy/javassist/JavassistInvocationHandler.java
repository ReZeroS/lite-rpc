package io.github.rezeros.core.proxy.javassist;

import io.github.rezeros.core.client.RpcReferenceWrapper;
import io.github.rezeros.core.concurrent.TimeoutInvocation;
import io.github.rezeros.protocol.RpcInvocation;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static io.github.rezeros.core.common.cache.CommonClientCache.*;

public class JavassistInvocationHandler implements InvocationHandler {

    private final RpcReferenceWrapper<?> rpcReferenceWrapper;

    public JavassistInvocationHandler(RpcReferenceWrapper<?> rpcReferenceWrapper) {
        this.rpcReferenceWrapper = rpcReferenceWrapper;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        RpcInvocation rpcInvocation = new RpcInvocation();
        rpcInvocation.setArgs(args);
        rpcInvocation.setTargetMethod(method.getName());
        rpcInvocation.setTargetServiceName(rpcReferenceWrapper.getAimClass().getName());
        rpcInvocation.setUuid(UUID.randomUUID().toString());
        return tryFinishedTask(rpcInvocation, CLIENT_CONFIG.getTimeout());
    }

    private static Object tryFinishedTask(RpcInvocation rpcInvocation, long timeout) throws InterruptedException, TimeoutException {
        //如果请求数据在指定时间内返回则返回给客户端调用方
        TimeoutInvocation timeoutInvocation = new TimeoutInvocation(null);
        RESP_MAP.put(rpcInvocation.getUuid(), timeoutInvocation);
        //这里就是将请求的参数放入到发送队列中
        SEND_QUEUE.add(rpcInvocation);
        if (timeoutInvocation.tryAcquire(timeout, TimeUnit.MILLISECONDS)){
            return RESP_MAP.remove(rpcInvocation.getUuid()).getRpcInvocation().getResponse();
        }
        RESP_MAP.remove(rpcInvocation.getUuid());
        throw new TimeoutException("client wait server's response timeout!");
    }
}