package io.github.rezeros.core.filter;

import io.github.rezeros.core.common.ChannelFutureWrapper;
import io.github.rezeros.protocol.RpcInvocation;

import java.util.List;

public interface IClientFilter extends IFilter {

    void doFilter(List<ChannelFutureWrapper> src, RpcInvocation rpcInvocation);
}
