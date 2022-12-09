package io.github.rezeros.core.filter.server;


import io.github.rezeros.core.filter.IServerFilter;
import io.github.rezeros.protocol.RpcInvocation;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author linhao
 * @Date created in 8:40 下午 2022/1/29
 */
public class ServerAfterFilterChain {

    private static final List<IServerFilter> iServerFilters = new ArrayList<>();

    public void addServerFilter(IServerFilter iServerFilter) {
        iServerFilters.add(iServerFilter);
    }

    public void doFilter(RpcInvocation rpcInvocation) {
        for (IServerFilter iServerFilter : iServerFilters) {
            iServerFilter.doFilter(rpcInvocation);
        }
    }
}
