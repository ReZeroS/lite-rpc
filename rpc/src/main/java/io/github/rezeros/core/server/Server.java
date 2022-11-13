package io.github.rezeros.core.server;

import io.github.rezeros.common.RpcDecoder;
import io.github.rezeros.common.RpcEncoder;
import io.github.rezeros.common.ServerHandler;
import io.github.rezeros.core.config.ServerConfig;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import static io.github.rezeros.cache.CommonServerCache.PROVIDER_CACHE;

public class Server {

    public static void main(String[] args) throws InterruptedException {
        Server server = new Server();
        ServerConfig serverConfig = new ServerConfig();
        serverConfig.setPort(9090);
        server.setServerConfig(serverConfig);
        server.registryService(new DataServiceImpl());
        server.startApplication();
    }

    private static EventLoopGroup bossGroup;
    private static EventLoopGroup workerGroup;
    private static ServerConfig serverConfig;



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
        serverBootstrap.bind(serverConfig.getPort()).sync();
    }

    private void registryService(Object serviceBean) {
        Class<?>[] interfaces = serviceBean.getClass().getInterfaces();
        if (interfaces.length != 1) {
            throw new RuntimeException("必须实现且仅实现一个接口？");
        }
        Class<?> anInterface = interfaces[0];
        PROVIDER_CACHE.put(anInterface.getName(), serviceBean);
    }

    private void setServerConfig(ServerConfig serverConfig) {
        serverConfig = serverConfig;
    }




}
