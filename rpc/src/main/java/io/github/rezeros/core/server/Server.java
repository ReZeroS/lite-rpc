package io.github.rezeros.core.server;

import io.github.rezeros.core.common.RpcDecoder;
import io.github.rezeros.core.common.RpcEncoder;
import io.github.rezeros.core.common.ServerHandler;
import io.github.rezeros.core.common.ServerServiceSemaphoreWrapper;
import io.github.rezeros.core.common.annotations.IRpcService;
import io.github.rezeros.core.common.annotations.SPI;
import io.github.rezeros.core.common.config.PropertiesBootstrap;
import io.github.rezeros.core.common.event.IRpcListenerLoader;
import io.github.rezeros.core.common.utils.CommonUtils;
import io.github.rezeros.core.config.ServerConfig;
import io.github.rezeros.core.filter.IServerFilter;
import io.github.rezeros.core.registry.AbstractRegister;
import io.github.rezeros.core.registry.RegistryService;
import io.github.rezeros.core.registry.URL;
import io.github.rezeros.core.serialize.SerializeFactory;
import io.github.rezeros.test.DataServiceImpl;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.util.concurrent.DefaultThreadFactory;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ThreadFactory;

import static io.github.rezeros.core.common.cache.CommonClientCache.EXTENSION_LOADER;
import static io.github.rezeros.core.common.cache.CommonServerCache.*;
import static io.github.rezeros.core.common.constants.RpcConstants.DEFAULT_DECODE_CHAR;
import static io.github.rezeros.core.spi.ExtensionLoader.EXTENSION_LOADER_CLASS_CACHE;

@Slf4j
public class Server {

    public static void main(String[] args) throws InterruptedException {
        Server server = new Server();
        server.initServerConfig();
        IRpcListenerLoader iRpcListenerLoader = new IRpcListenerLoader();
        iRpcListenerLoader.init();
        // 暴露服务信息
        server.exportService(new ServiceWrapper(new DataServiceImpl()));
        // 停机hook
        ApplicationShutdownHook.registryShutdownHook();

        server.startApplication();
        log.info("[startApplication] server is started!");

    }


    private void initServerConfig() throws Exception {
        this.serverConfig = PropertiesBootstrap.loadServerConfigFromLocal();
        // 业务线程相关配置
        SERVER_CHANNEL_DISPATCHER.init(serverConfig.getServerQueueSize(), serverConfig.getServerBizThreadNums());

        // 序列化方式
        String serverSerialize = serverConfig.getServerSerialize();
        EXTENSION_LOADER.loadExtension(SerializeFactory.class);
        LinkedHashMap<String, Class<?>> serializeFactoryClassMap = EXTENSION_LOADER_CLASS_CACHE.get(SerializeFactory.class.getName());
        Class<?> serializeFactoryClass = serializeFactoryClassMap.get(serverSerialize);
        if (serializeFactoryClass == null) {
            throw new RuntimeException("no match serialize type for " + serverSerialize);
        }

        SERVER_SERIALIZE_FACTORY = (SerializeFactory) serializeFactoryClass.newInstance();
        //过滤链技术初始化
        EXTENSION_LOADER.loadExtension(IServerFilter.class);
        LinkedHashMap<String, Class<?>> iServerFilterClassMap = EXTENSION_LOADER_CLASS_CACHE.get(IServerFilter.class.getName());
        ServerBeforeFilterChain serverBeforeFilterChain = new ServerBeforeFilterChain();
        ServerAfterFilterChain serverAfterFilterChain = new ServerAfterFilterChain();
        //过滤器初始化环节新增 前置过滤器和后置过滤器
        for (String iServerFilterKey : iServerFilterClassMap.keySet()) {
            Class iServerFilterClass = iServerFilterClassMap.get(iServerFilterKey);
            if (iServerFilterClass == null) {
                throw new RuntimeException("no match iServerFilter type for " + iServerFilterKey);
            }
            SPI spi = (SPI) iServerFilterClass.getDeclaredAnnotation(SPI.class);
            if (spi != null && "before".equals(spi.value())) {
                serverBeforeFilterChain.addServerFilter((IServerFilter) iServerFilterClass.newInstance());
            } else if (spi != null && "after".equals(spi.value())) {
                serverAfterFilterChain.addServerFilter((IServerFilter) iServerFilterClass.newInstance());
            }
        }
        SERVER_AFTER_FILTER_CHAIN = serverAfterFilterChain;
        SERVER_BEFORE_FILTER_CHAIN = serverBeforeFilterChain;
    }

    private ServerConfig serverConfig;


    private void startApplication() throws InterruptedException {
        ThreadFactory threadFactory = new DefaultThreadFactory("irpc-NettyServerWorker", true);
        int core = Runtime.getRuntime().availableProcessors() + 1;
        log.info("This node has {} available processors ", core);
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(bossGroup, workerGroup);
        serverBootstrap.channel(NioServerSocketChannel.class);
        serverBootstrap.option(ChannelOption.TCP_NODELAY, true)
                .option(ChannelOption.SO_BACKLOG, 1024)
                .option(ChannelOption.SO_SNDBUF, 16 * 1024)
                .option(ChannelOption.SO_RCVBUF, 16 * 1024)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .option(ChannelOption.SO_REUSEADDR, true);

        // 长链接模式
        // 绑在 accept 上？
        serverBootstrap.handler(new MaxConnectionLimitHandler(serverConfig.getMaxConnections()));

        serverBootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                log.info("初始化provider过程");
                ByteBuf delimiter = Unpooled.copiedBuffer(DEFAULT_DECODE_CHAR.getBytes());
                ch.pipeline().addLast(new DelimiterBasedFrameDecoder(serverConfig.getMaxServerRequestData(), delimiter));
                ch.pipeline().addLast(new RpcEncoder());
                ch.pipeline().addLast(new RpcDecoder());
                ch.pipeline().addLast(new ServerHandler());
            }
        });
        this.batchExportUrl();
        SERVER_CHANNEL_DISPATCHER.startDataConsume();
        serverBootstrap.bind(serverConfig.getServerPort()).sync();
    }

    private void batchExportUrl() {
        new Thread(() -> {
            for (URL url : PROVIDER_URL_SET) {
                REGISTRY_SERVICE.register(url);
            }
        }).start();
    }


    private void exportService(ServiceWrapper serviceWrapper) {
        if (REGISTRY_SERVICE == null) {
            try {
                EXTENSION_LOADER.loadExtension(RegistryService.class);
                Map<String, Class<?>> registryClassMap = EXTENSION_LOADER_CLASS_CACHE.get(RegistryService.class.getName());
                Class<?> registryClass = registryClassMap.get(serverConfig.getRegisterType());
                REGISTRY_SERVICE = (AbstractRegister) registryClass.newInstance();
            } catch (Exception e) {
                throw new RuntimeException("registryServiceType unKnow,error is ", e);
            }
        }
        Object serviceBean = serviceWrapper.getServiceObj();
        Class<?>[] interfaceClasses = serviceBean.getClass().getInterfaces();
        Arrays.stream(interfaceClasses).filter(e -> e.isAnnotationPresent(IRpcService.class))
                .forEach(interfaceClass -> {
                    PROVIDER_CACHE.put(interfaceClass.getName(), serviceBean);
                    URL url = new URL();
                    url.setServiceName(interfaceClass.getName());
                    url.setApplicationName(serverConfig.getApplicationName());
                    url.addParameter("host", CommonUtils.getIpAddress());
                    url.addParameter("port", String.valueOf(serverConfig.getServerPort()));

                    // todo 服务分组应该暴露在外部存储，由外部存储决定组织
                    // todo 比如 提供一个面板可以让 【A B】Service作为 dev分组，【B C】Service 作为 test分组
                    // todo 这样客户端调用的时候先确认自己需要什么分组（同样外部存储定），然后从 外部存储拿信息决定调用哪个
                    url.addParameter("group", String.valueOf(serviceWrapper.getGroup()));
                    url.addParameter("limit", String.valueOf(serviceWrapper.getLimit()));
                    PROVIDER_URL_SET.add(url);
                    //设置服务端的限流器
                    SERVER_SERVICE_SEMAPHORE_MAP.put(interfaceClass.getName(), new ServerServiceSemaphoreWrapper(serviceWrapper.getLimit()));
                });
    }


}
