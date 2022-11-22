package io.github.rezeros.core.router;

import io.github.rezeros.core.common.ChannelFutureWrapper;
import io.github.rezeros.core.registry.URL;

import java.util.List;

import static io.github.rezeros.core.common.cache.CommonClientCache.CHANNEL_FUTURE_POLLING_REF;
import static io.github.rezeros.core.common.cache.CommonClientCache.CONNECT_MAP;
import static io.github.rezeros.core.common.cache.CommonClientCache.SERVICE_ROUTER_MAP;

public class RotateRouterImpl implements IRouter {


    @Override
    public void refreshRouterArr(Selector selector) {
        List<ChannelFutureWrapper> channelFutureWrappers = CONNECT_MAP.get(selector.getProviderServiceName());
        ChannelFutureWrapper[] arr = new ChannelFutureWrapper[channelFutureWrappers.size()];
        for (int i = 0; i < channelFutureWrappers.size(); i++) {
            arr[i] = channelFutureWrappers.get(i);
        }
        SERVICE_ROUTER_MAP.put(selector.getProviderServiceName(), arr);
    }

    @Override
    public ChannelFutureWrapper select(Selector selector) {
        return CHANNEL_FUTURE_POLLING_REF.getChannelFutureWrapper(selector.getProviderServiceName());
    }

    @Override
    public void updateWeight(URL url) {

    }
}