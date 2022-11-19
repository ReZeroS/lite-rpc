package io.github.rezeros.core.filter.server;

import io.github.rezeros.core.filter.IServerFilter;
import io.github.rezeros.protocol.RpcInvocation;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ServerLogFilterImpl implements IServerFilter {


    @Override
    public void doFilter(RpcInvocation rpcInvocation) {
        log.info(rpcInvocation.getAttachments().get("c_app_name") + " do invoke -----> " + rpcInvocation.getTargetServiceName() + "#" + rpcInvocation.getTargetMethod());
    }

}