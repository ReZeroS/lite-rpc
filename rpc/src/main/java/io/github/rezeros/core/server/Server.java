package io.github.rezeros.core.server;

import io.github.rezeros.core.common.RpcDecoder;
import io.github.rezeros.core.common.RpcEncoder;
import io.github.rezeros.core.common.ServerHandler;
import io.github.rezeros.core.common.config.PropertiesBootstrap;
import io.github.rezeros.core.common.event.IRpcListenerLoader;
import io.github.rezeros.core.common.utils.CommonUtils;
import io.github.rezeros.core.config.ServerConfig;
import io.github.rezeros.core.registry.RegistryService;
import io.github.rezeros.core.registry.URL;
import io.github.rezeros.core.registry.zookeeper.ZookeeperRegister;
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
import lombok.extern.slf4j.Slf4j;

import static io.github.rezeros.core.common.cache.CommonServerCache.PROVIDER_CACHE;
import static io.github.rezeros.core.common.cache.CommonServerCache.PROVIDER_URL_SET;
import static io.github.rezeros.core.common.constants.RpcConstants.DEFAULT_DECODE_CHAR;

@Slf4j
public class Server {

    private static IRpcListenerLoader iRpcListenerLoader;

    public static void main(String[] args) throws InterruptedException {
        Server server = new Server();
        server.initServerConfig();
        iRpcListenerLoader = new IRpcListenerLoader();
        iRpcListenerLoader.init();
        // 暴露服务信息
        server.exportService(new ServiceWrapper(new DataServiceImpl()));
        // 停机hook
        ApplicationShutdownHook.registryShutdownHook();

        server.startApplication();
        log.info("[startApplication] server is started!");

    }


    private void initServerConfig() {
        this.serverConfig = PropertiesBootstrap.loadServerConfigFromLocal();
    }

    private static EventLoopGroup bossGroup;
    private static EventLoopGroup workerGroup;

    private ServerConfig serverConfig;

    private RegistryService registryService;


    private void startApplication() throws InterruptedException {
        bossGroup = new NioEventLoopGroup();
        workerGroup = new NioEventLoopGroup();
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
                System.out.println("初始化provider过程");
                ByteBuf delimiter = Unpooled.copiedBuffer(DEFAULT_DECODE_CHAR.getBytes());
                ch.pipeline().addLast(new DelimiterBasedFrameDecoder(serverConfig.getMaxServerRequestData(), delimiter));
                ch.pipeline().addLast(new RpcEncoder());
                ch.pipeline().addLast(new RpcDecoder());
                ch.pipeline().addLast(new ServerHandler());
            }
        });
        this.batchExportUrl();
        serverBootstrap.bind(serverConfig.getServerPort()).sync();
    }

    private void batchExportUrl() {
        new Thread(() -> {
            for (URL url : PROVIDER_URL_SET) {
                registryService.register(url);
            }
        }).start();
    }


    private void exportService(Object serviceBean) {
        Class<?>[] interfaces = serviceBean.getClass().getInterfaces();
        if (interfaces.length != 1) {
            throw new RuntimeException("必须实现且仅实现一个接口");
        }
        if (registryService == null) {
            registryService = new ZookeeperRegister(serverConfig.getRegisterAddr());
        }
        Class<?> interfaceClass = interfaces[0];

        PROVIDER_CACHE.put(interfaceClass.getName(), serviceBean);

        URL url = new URL();
        url.setServiceName(interfaceClass.getName());
        url.setApplicationName(serverConfig.getApplicationName());
        url.addParameter("host", CommonUtils.getIpAddress());
        url.addParameter("port", String.valueOf(serverConfig.getServerPort()));

        PROVIDER_URL_SET.add(url);
    }


}
