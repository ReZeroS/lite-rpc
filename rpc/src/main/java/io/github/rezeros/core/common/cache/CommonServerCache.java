package io.github.rezeros.core.common.cache;

import io.github.rezeros.core.common.ServerServiceSemaphoreWrapper;
import io.github.rezeros.core.dispatcher.ServerChannelDispatcher;
import io.github.rezeros.core.filter.server.ServerFilterChain;
import io.github.rezeros.core.registry.RegistryService;
import io.github.rezeros.core.registry.URL;
import io.github.rezeros.core.serialize.SerializeFactory;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class CommonServerCache {

    public static final Map<String,Object> PROVIDER_CACHE = new HashMap<>();
    public static final Set<URL> PROVIDER_URL_SET = new HashSet<>();


    public static SerializeFactory SERVER_SERIALIZE_FACTORY;

    public static ServerFilterChain SERVER_FILTER_CHAIN;


    public static ServerChannelDispatcher SERVER_CHANNEL_DISPATCHER;

    public static final Map<String, ServerServiceSemaphoreWrapper> SERVER_SERVICE_SEMAPHORE_MAP = new ConcurrentHashMap<>(64);

    public static RegistryService REGISTRY_SERVICE;


}