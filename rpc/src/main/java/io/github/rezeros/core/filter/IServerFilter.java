package io.github.rezeros.core.filter;

import io.github.rezeros.protocol.RpcInvocation;

public interface IServerFilter extends IFilter {

    /**
     * 执行核心过滤逻辑
     */
    void doFilter(RpcInvocation rpcInvocation);

}