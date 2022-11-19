package io.github.rezeros.core.filter.client;

import io.github.rezeros.core.common.ChannelFutureWrapper;
import io.github.rezeros.core.filter.IClientFilter;
import io.github.rezeros.protocol.RpcInvocation;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.logging.LogRecord;

import static io.github.rezeros.cache.CommonClientCache.CLIENT_CONFIG;

@Slf4j
public class ClientLogFilterImpl implements IClientFilter {

    @Override
    public void doFilter(List<ChannelFutureWrapper> src, RpcInvocation rpcInvocation) {
        rpcInvocation.getAttachments().put("c_app_name", CLIENT_CONFIG.getApplicationName());
        log.info(rpcInvocation.getAttachments().get("c_app_name") + " do invoke -----> " + rpcInvocation.getTargetServiceName());
    }


    @Override
    public boolean isLoggable(LogRecord record) {
        return false;
    }
}