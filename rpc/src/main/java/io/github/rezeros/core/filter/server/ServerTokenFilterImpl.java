package io.github.rezeros.core.filter.server;

import io.github.rezeros.core.filter.IServerFilter;
import io.github.rezeros.protocol.RpcInvocation;

public class ServerTokenFilterImpl implements IServerFilter {

    @Override
    public void doFilter(RpcInvocation rpcInvocation) {
//        String token = String.valueOf(rpcInvocation.getAttachments().get("serviceToken"));
//        ServiceWrapper serviceWrapper = PROVIDER_SERVICE_WRAPPER_MAP.get(rpcInvocation.getTargetServiceName());
//        String matchToken = String.valueOf(serviceWrapper.getServiceToken());
//        if (CommonUtils.isEmpty(matchToken)) {
//            return;
//        }
//        if (!CommonUtils.isEmpty(token) && token.equals(matchToken)) {
//            return;
//        }
//        throw new RuntimeException("token is " + token + " , verify result is false!");
    }
}