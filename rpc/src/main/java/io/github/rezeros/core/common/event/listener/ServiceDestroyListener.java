package io.github.rezeros.core.common.event.listener;

import io.github.rezeros.core.common.event.IRpcDestroyEvent;
import io.github.rezeros.core.common.event.IRpcListener;
import io.github.rezeros.core.registry.URL;

import static io.github.rezeros.core.common.cache.CommonServerCache.PROVIDER_URL_SET;
import static io.github.rezeros.core.common.cache.CommonServerCache.REGISTRY_SERVICE;

public class ServiceDestroyListener implements IRpcListener<IRpcDestroyEvent> {

    @Override
    public void callBack(Object t) {
        for (URL url : PROVIDER_URL_SET) {
            REGISTRY_SERVICE.unRegister(url);
        }
    }
}