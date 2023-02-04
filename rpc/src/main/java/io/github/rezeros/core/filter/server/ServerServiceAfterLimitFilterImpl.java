package io.github.rezeros.core.filter.server;

import io.github.rezeros.core.common.ServerServiceSemaphoreWrapper;
import io.github.rezeros.core.common.annotations.SPI;
import io.github.rezeros.core.filter.IServerFilter;
import io.github.rezeros.protocol.RpcInvocation;

import static io.github.rezeros.core.common.cache.CommonServerCache.SERVER_SERVICE_SEMAPHORE_MAP;

@SPI("after")
public class ServerServiceAfterLimitFilterImpl implements IServerFilter {

    @Override
    public void doFilter(RpcInvocation rpcInvocation) {
        String serviceName = rpcInvocation.getTargetServiceName();
        ServerServiceSemaphoreWrapper serverServiceSemaphoreWrapper = SERVER_SERVICE_SEMAPHORE_MAP.get(serviceName);
        serverServiceSemaphoreWrapper.getSemaphore().release();
    }
}