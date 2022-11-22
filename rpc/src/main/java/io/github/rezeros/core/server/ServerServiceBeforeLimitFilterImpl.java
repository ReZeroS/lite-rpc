package io.github.rezeros.core.server;

import io.github.rezeros.core.common.ServerServiceSemaphoreWrapper;
import io.github.rezeros.core.common.annotations.SPI;
import io.github.rezeros.core.common.exception.MaxServiceLimitRequestException;
import io.github.rezeros.core.filter.IServerFilter;
import io.github.rezeros.protocol.RpcInvocation;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Semaphore;

import static io.github.rezeros.core.common.cache.CommonServerCache.SERVER_SERVICE_SEMAPHORE_MAP;

@Slf4j
@SPI("before")
public class ServerServiceBeforeLimitFilterImpl implements IServerFilter {

    @Override
    public void doFilter(RpcInvocation rpcInvocation) {
        String serviceName = rpcInvocation.getTargetServiceName();
        ServerServiceSemaphoreWrapper serverServiceSemaphoreWrapper = SERVER_SERVICE_SEMAPHORE_MAP.get(serviceName);
        //从缓存中提取semaphore对象
        Semaphore semaphore = serverServiceSemaphoreWrapper.getSemaphore();
        boolean tryResult = semaphore.tryAcquire();
        if (!tryResult) {
            log.error("[ServerServiceBeforeLimitFilterImpl] {}'s max request is {},reject now", rpcInvocation.getTargetServiceName(), serverServiceSemaphoreWrapper.getMaxNums());
            MaxServiceLimitRequestException iRpcException = new MaxServiceLimitRequestException(rpcInvocation);
            rpcInvocation.setE(iRpcException);
            throw iRpcException;
        }
    }
}