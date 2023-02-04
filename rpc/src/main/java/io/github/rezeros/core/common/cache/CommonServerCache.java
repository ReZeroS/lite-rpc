package io.github.rezeros.core.common.cache;

import io.github.rezeros.core.common.ServerServiceSemaphoreWrapper;
import io.github.rezeros.core.config.ServerConfig;
import io.github.rezeros.core.dispatcher.ServerChannelDispatcher;
import io.github.rezeros.core.filter.server.ServerAfterFilterChain;
import io.github.rezeros.core.filter.server.ServerBeforeFilterChain;
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

    public static ServerBeforeFilterChain SERVER_BEFORE_FILTER_CHAIN;
    public static ServerAfterFilterChain SERVER_AFTER_FILTER_CHAIN;


    public static ServerChannelDispatcher SERVER_CHANNEL_DISPATCHER = new ServerChannelDispatcher();

    public static final Map<String, ServerServiceSemaphoreWrapper> SERVER_SERVICE_SEMAPHORE_MAP = new ConcurrentHashMap<>(64);

    public static RegistryService REGISTRY_SERVICE;

    public static volatile boolean IS_STARTED;

    public static ServerConfig SERVER_CONFIG;

}