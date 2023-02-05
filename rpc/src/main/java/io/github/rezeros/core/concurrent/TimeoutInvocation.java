package io.github.rezeros.core.concurrent;

import io.github.rezeros.protocol.RpcInvocation;
import lombok.Data;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@Data
public class TimeoutInvocation {

    private final CountDownLatch latch;

    private RpcInvocation rpcInvocation;

    public TimeoutInvocation(RpcInvocation rpcInvocation) {
        this.rpcInvocation = rpcInvocation;
        this.latch = new CountDownLatch(1);
    }

    public void setRpcInvocation(RpcInvocation rpcInvocation){
        this.rpcInvocation = rpcInvocation;
    }

    public RpcInvocation getRpcInvocation() {
        return rpcInvocation;
    }

    public boolean tryAcquire(long timeout, TimeUnit unit) throws InterruptedException {
        return latch.await(timeout, unit);
    }

    public void release(){
        latch.countDown();
    }
}
