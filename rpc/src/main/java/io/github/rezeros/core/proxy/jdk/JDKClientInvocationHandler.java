package io.github.rezeros.core.proxy.jdk;

import io.github.rezeros.protocol.RpcInvocation;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.UUID;
import java.util.concurrent.TimeoutException;

import static io.github.rezeros.cache.CommonClientCache.RESP_MAP;
import static io.github.rezeros.cache.CommonClientCache.SEND_QUEUE;

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
        RESP_MAP.put(rpcInvocation.getUuid(), OBJECT);
        //这里就是将请求的参数放入到发送队列中
        SEND_QUEUE.add(rpcInvocation);
        long beginTime = System.currentTimeMillis();
        //客户端请求超时的一个判断依据
        while (System.currentTimeMillis() - beginTime < 3*1000) {
            Object object = RESP_MAP.get(rpcInvocation.getUuid());
            if (object instanceof RpcInvocation) {
                return ((RpcInvocation)object).getResponse();
            }
        }
        throw new TimeoutException("client wait server's response timeout!");
    }
}
