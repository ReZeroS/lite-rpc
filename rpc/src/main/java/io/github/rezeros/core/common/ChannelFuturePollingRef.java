package io.github.rezeros.core.common;

import java.util.concurrent.atomic.AtomicLong;

import static io.github.rezeros.core.common.cache.CommonClientCache.SERVICE_ROUTER_MAP;

public class ChannelFuturePollingRef {
    private AtomicLong referenceTimes = new AtomicLong(0);


    public ChannelFutureWrapper getChannelFutureWrapper(String serviceName){
        ChannelFutureWrapper[] arr = SERVICE_ROUTER_MAP.get(serviceName);
        long i = referenceTimes.getAndIncrement();
        int index = (int) (i % arr.length);
        return arr[index];
    }


}
