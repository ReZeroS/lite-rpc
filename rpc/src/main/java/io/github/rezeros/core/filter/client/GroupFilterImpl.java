package io.github.rezeros.core.filter.client;

import io.github.rezeros.core.common.ChannelFutureWrapper;
import io.github.rezeros.core.common.utils.CommonUtils;
import io.github.rezeros.core.filter.IClientFilter;
import io.github.rezeros.protocol.RpcInvocation;

import java.util.List;

public class GroupFilterImpl implements IClientFilter {


    @Override
    public void doFilter(List<ChannelFutureWrapper> src, RpcInvocation rpcInvocation) {
        String group = String.valueOf(rpcInvocation.getAttachments().get("group"));
        for (ChannelFutureWrapper channelFutureWrapper : src) {
            if (!channelFutureWrapper.getGroup().equals(group)) {
                src.remove(channelFutureWrapper);
            }
        }
        if (CommonUtils.isEmptyList(src)) {
            throw new RuntimeException("no provider match for group " + group);
        }
    }


}
