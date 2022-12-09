package io.github.rezeros.core.client;


import com.alibaba.fastjson.JSON;
import io.github.rezeros.core.common.RpcDecoder;
import io.github.rezeros.core.common.RpcEncoder;
import io.github.rezeros.core.common.cache.CommonClientCache;
import io.github.rezeros.core.common.config.PropertiesBootstrap;
import io.github.rezeros.core.common.event.IRpcListenerLoader;
import io.github.rezeros.core.common.utils.CommonUtils;
import io.github.rezeros.core.config.ClientConfig;
import io.github.rezeros.core.filter.IClientFilter;
import io.github.rezeros.core.filter.client.ClientFilterChain;
import io.github.rezeros.core.proxy.javassist.JavassistProxyFactory;
import io.github.rezeros.core.proxy.jdk.JDKProxyFactory;
import io.github.rezeros.core.registry.AbstractRegister;
import io.github.rezeros.core.registry.RegistryService;
import io.github.rezeros.core.registry.URL;
import io.github.rezeros.core.registry.zookeeper.ZookeeperRegister;
import io.github.rezeros.core.router.IRouter;
import io.github.rezeros.core.serialize.SerializeFactory;
import io.github.rezeros.protocol.RpcInvocation;
import io.github.rezeros.protocol.RpcProtocol;
import io.github.rezeros.test.DataService;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static io.github.rezeros.core.common.cache.CommonClientCache.EXTENSION_LOADER;
import static io.github.rezeros.core.common.cache.CommonClientCache.SEND_QUEUE;
import static io.github.rezeros.core.common.cache.CommonClientCache.SUBSCRIBE_SERVICE_LIST;
import static io.github.rezeros.core.common.cache.CommonClientCache.IROUTER;
import static io.github.rezeros.core.common.cache.CommonClientCache.CLIENT_SERIALIZE_FACTORY;


import static io.github.rezeros.core.common.constants.RpcConstants.DEFAULT_DECODE_CHAR;

import static io.github.rezeros.core.spi.ExtensionLoader.EXTENSION_LOADER_CLASS_CACHE;

@Slf4j
@Data
public class Client {


    private static final EventLoopGroup clientGroup = new NioEventLoopGroup();


    private ClientConfig clientConfig;

    private AbstractRegister abstractRegister;

    private IRpcListenerLoader iRpcListenerLoader;

    private Bootstrap bootstrap = new Bootstrap();



    public static void main(String[] args) throws Throwable {
        Client client = new Client();
        RpcReference rpcReference = client.initClientApplication();
        RpcReferenceWrapper<DataService> rpcReferenceWrapper = new RpcReferenceWrapper<>();
        rpcReferenceWrapper.setAimClass(DataService.class);
        rpcReferenceWrapper.setGroup("dev");
//        rpcReferenceWrapper.setUrl("192.168.43.227:9093");
        //在初始化之前必须要设置对应的上下文
        DataService dataService = rpcReference.get(rpcReferenceWrapper);
        client.doSubscribeService(DataService.class);
        ConnectionHandler.setBootstrap(client.getBootstrap());
        client.doConnectServer();
        client.startClient();
        for (int i = 0; i < 10000; i++) {
            try {
                String result = dataService.sendData("test");
                System.out.println(result);
                Thread.sleep(1000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void doConnectServer() {
        for (String providerServiceName : SUBSCRIBE_SERVICE_LIST) {
            List<String> providerIps = abstractRegister.getProviderIps(providerServiceName);
            for (String providerIp : providerIps) {
                try {
                    ConnectionHandler.connect(providerServiceName, providerIp);
                } catch (InterruptedException e) {
                    log.error("Client connect providerService {} failed. provider ip is: {}", providerServiceName, providerIp);
                    throw new RuntimeException(e);
                }
            }

            URL url = new URL();
            url.setServiceName(providerServiceName);
            //客户端在此新增一个订阅的功能
            abstractRegister.doAfterSubscribe(url);
        }
    }

    public void doSubscribeService(Class serviceBean) {
        if (abstractRegister == null) {
            try {
                //使用自定义的SPI机制去加载配置
                EXTENSION_LOADER.loadExtension(RegistryService.class);
                Map<String, Class<?>> registerMap = EXTENSION_LOADER_CLASS_CACHE.get(RegistryService.class.getName());
                Class<?> registerClass =  registerMap.get(clientConfig.getRegisterType());
                //真正实力化对象的位置
                abstractRegister = (AbstractRegister) registerClass.newInstance();
            } catch (Exception e) {
                throw new RuntimeException("registryServiceType unKnow,error is ", e);
            }
        }

        URL url = new URL();
        url.setApplicationName(clientConfig.getApplicationName());
        url.setServiceName(serviceBean.getName());
        url.addParameter("host", CommonUtils.getIpAddress());
        abstractRegister.subscribe(url);
    }

    public RpcReference initClientApplication() throws InterruptedException {
        clientConfig = PropertiesBootstrap.loadClientConfigFromLocal();
        bootstrap.group(clientGroup);
        bootstrap.channel(NioSocketChannel.class);
        bootstrap.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                ByteBuf delimiter = Unpooled.copiedBuffer(DEFAULT_DECODE_CHAR.getBytes());
                ch.pipeline().addLast(new DelimiterBasedFrameDecoder(clientConfig.getMaxServerRespDataSize(), delimiter));
                //管道中初始化一些逻辑，这里包含了上边所说的编解码器和客户端响应类
                ch.pipeline().addLast(new RpcEncoder());
                ch.pipeline().addLast(new RpcDecoder());
                ch.pipeline().addLast(new ClientHandler());
            }
        });
        //常规的链接netty服务端
        iRpcListenerLoader = new IRpcListenerLoader();
        iRpcListenerLoader.init();

        return clientConfig.getProxyType().equals("javassist")
                ? new RpcReference(new JavassistProxyFactory())
                : new RpcReference(new JDKProxyFactory());
    }


    private void initClientConfig() throws IOException, IllegalAccessException, ClassNotFoundException, InstantiationException {
        //初始化路由策略
        EXTENSION_LOADER.loadExtension(IRouter.class);
        String routerStrategy = clientConfig.getRouterStrategy();
        LinkedHashMap<String, Class<?>> iRouterMap = EXTENSION_LOADER_CLASS_CACHE.get(IRouter.class.getName());
        Class iRouterClass = iRouterMap.get(routerStrategy);
        if (iRouterClass == null) {
            throw new RuntimeException("no match routerStrategy for " + routerStrategy);
        }
        IROUTER = (IRouter) iRouterClass.newInstance();

        //初始化序列化框架
        EXTENSION_LOADER.loadExtension(SerializeFactory.class);
        String clientSerialize = clientConfig.getClientSerialize();

        CLIENT_SERIALIZE_FACTORY = EXTENSION_LOADER.getInstance(clientSerialize, SerializeFactory.class);

        //初始化过滤链
        ClientFilterChain clientFilterChain = new ClientFilterChain();
        EXTENSION_LOADER.loadExtension(IClientFilter.class);
        List<IClientFilter> clientFilters = EXTENSION_LOADER.loadInstanceList(IClientFilter.class);
        for (IClientFilter clientFilter : clientFilters) {
            clientFilterChain.addClientFilter(clientFilter);
        }
        CommonClientCache.CLIENT_FILTER_CHAIN = clientFilterChain;
    }



    /**
     * 开启发送线程，专门从事将数据包发送给服务端，起到一个解耦的效果
     */
    public void startClient() {
        Thread asyncSendJob = new Thread(new AsyncSendJob());
        asyncSendJob.start();
    }

    /**
     * 异步发送信息任务
     */
    static class AsyncSendJob implements Runnable {

        @Override
        public void run() {
            while (true) {
                try {
                    //阻塞模式
                    RpcInvocation data = SEND_QUEUE.take();
                    //将RpcInvocation封装到RpcProtocol对象中，然后发送给服务端，这里正好对应了上文中的ServerHandler
                    String json = JSON.toJSONString(data);
                    RpcProtocol rpcProtocol = new RpcProtocol(json.getBytes());
                    ChannelFuture channelFuture = ConnectionHandler.getChannelFuture(data.getTargetServiceName());
                    //netty的通道负责发送数据给服务端
                    channelFuture.channel().writeAndFlush(rpcProtocol);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    return;
                }
            }
        }
    }

}
