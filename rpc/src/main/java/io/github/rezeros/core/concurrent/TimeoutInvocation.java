package io.github.rezeros.core.concurrent;

import io.github.rezeros.protocol.RpcInvocation;
import lombok.Data;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

@Data
public class TimeoutInvocation {

    private final Semaphore semaphore;

    private RpcInvocation rpcInvocation;

    public TimeoutInvocation(RpcInvocation rpcInvocation) throws InterruptedException {
        this.rpcInvocation = rpcInvocation;
        this.semaphore = new Semaphore(1);
        semaphore.acquire();
    }

    public void setRpcInvocation(RpcInvocation rpcInvocation){
        this.rpcInvocation = rpcInvocation;
    }

    public RpcInvocation getRpcInvocation() {
        return rpcInvocation;
    }

    public boolean tryAcquire(long timeout, TimeUnit unit) throws InterruptedException {
        return semaphore.tryAcquire(timeout, unit);
    }

    public void release(){
        semaphore.release();
    }
}
