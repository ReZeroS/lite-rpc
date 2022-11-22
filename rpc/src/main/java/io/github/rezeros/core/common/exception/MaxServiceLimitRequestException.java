package io.github.rezeros.core.common.exception;

import io.github.rezeros.protocol.RpcInvocation;

public class MaxServiceLimitRequestException extends IRpcException{

    public MaxServiceLimitRequestException(RpcInvocation rpcInvocation) {
        super(rpcInvocation);
    }
}