package io.github.rezeros.core.filter.client;

import io.github.rezeros.core.common.ChannelFutureWrapper;
import io.github.rezeros.core.filter.IClientFilter;
import io.github.rezeros.protocol.RpcInvocation;

import java.util.ArrayList;
import java.util.List;

public class ClientFilterChain {

    private static final List<IClientFilter> iClientFilterList = new ArrayList<>();

    public void addClientFilter(IClientFilter iClientFilter) {
        iClientFilterList.add(iClientFilter);
    }

    public void doFilter(List<ChannelFutureWrapper> src, RpcInvocation rpcInvocation) {
        for (IClientFilter iClientFilter : iClientFilterList) {
            iClientFilter.doFilter(src, rpcInvocation);
        }
    }

}