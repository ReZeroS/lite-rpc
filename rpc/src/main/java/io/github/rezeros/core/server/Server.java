package io.github.rezeros.core.server;

import io.github.rezeros.core.common.RpcDecoder;
import io.github.rezeros.core.common.RpcEncoder;
import io.github.rezeros.core.common.ServerHandler;
import io.github.rezeros.core.config.ServerConfig;
import io.github.rezeros.core.registry.RegistryService;
import io.github.rezeros.core.registry.URL;
import io.github.rezeros.core.registry.zookeeper.ZookeeperRegister;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import static io.github.rezeros.cache.CommonServerCache.PROVIDER_CACHE;
import static io.github.rezeros.cache.CommonServerCache.PROVIDER_URL_SET;

public class Server {

    public static void main(String[] args) throws InterruptedException {
        Server server = new Server();
        server.initServerConfig();
        server.registryService(new DataServiceImpl());
        server.startApplication();
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
        serverBootstrap.option(ChannelOption.TCP_NODELAY, true);
        serverBootstrap.option(ChannelOption.SO_BACKLOG, 1024);
        serverBootstrap.option(ChannelOption.SO_SNDBUF, 16 * 1024)
                .option(ChannelOption.SO_RCVBUF, 16 * 1024)
                .option(ChannelOption.SO_KEEPALIVE, true);

        serverBootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                System.out.println("初始化provider过程");
                ch.pipeline().addLast(new RpcEncoder());
                ch.pipeline().addLast(new RpcDecoder());
                ch.pipeline().addLast(new ServerHandler());
            }
        });
        this.batchExportUrl();
        serverBootstrap.bind(serverConfig.getPort()).sync();
    }

    private void batchExportUrl() {
        new Thread(() -> {
            for (URL url : PROVIDER_URL_SET) {
                registryService.register(url);
            }
        }).start();
    }

    private void registryService(Object serviceBean) {
        Class<?>[] interfaces = serviceBean.getClass().getInterfaces();
        if (interfaces.length != 1) {
            throw new RuntimeException("必须实现且仅实现一个接口？");
        }
        if (registryService == null) {
            registryService = new ZookeeperRegister(serverConfig.getRegisterAddr())
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
