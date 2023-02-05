package io.github.rezeros.core.common.cache;

import io.github.rezeros.core.common.ChannelFuturePollingRef;
import io.github.rezeros.core.common.ChannelFutureWrapper;
import io.github.rezeros.core.concurrent.TimeoutInvocation;
import io.github.rezeros.core.config.ClientConfig;
import io.github.rezeros.core.filter.client.ClientFilterChain;
import io.github.rezeros.core.registry.AbstractRegister;
import io.github.rezeros.core.registry.URL;
import io.github.rezeros.core.router.IRouter;
import io.github.rezeros.core.serialize.SerializeFactory;
import io.github.rezeros.core.spi.ExtensionLoader;
import io.github.rezeros.protocol.RpcInvocation;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

public class CommonClientCache {

    public static BlockingQueue<RpcInvocation> SEND_QUEUE = new ArrayBlockingQueue<>(100);
    public static ConcurrentHashMap<String, TimeoutInvocation> RESP_MAP = new ConcurrentHashMap<>();
    public static ClientConfig CLIENT_CONFIG;
    //provider名称 --> 该服务有哪些集群URL
    public static List<URL> SUBSCRIBE_SERVICE_LIST = new ArrayList<>();
    public static Map<String, List<URL>> URL_MAP = new ConcurrentHashMap<>();
    public static Set<String> SERVER_ADDRESS = new HashSet<>();
    //每次进行远程调用的时候都是从这里面去选择服务提供者
    public static Map<String, List<ChannelFutureWrapper>> CONNECT_MAP = new ConcurrentHashMap<>();
    public static ChannelFuturePollingRef CHANNEL_FUTURE_POLLING_REF = new ChannelFuturePollingRef();


    public static ClientFilterChain clientFilterChain;

    public static IRouter IROUTER;

    public static SerializeFactory CLIENT_SERIALIZE_FACTORY;


    public static Map<String, ChannelFutureWrapper[]> SERVICE_ROUTER_MAP = new ConcurrentHashMap<>();


    public static ExtensionLoader EXTENSION_LOADER = new ExtensionLoader();

    public static ClientFilterChain CLIENT_FILTER_CHAIN;

    public static AbstractRegister ABSTRACT_REGISTER;

}