package io.github.rezeros.core.client;


import com.alibaba.fastjson.JSON;
import io.github.rezeros.core.common.RpcDecoder;
import io.github.rezeros.core.common.RpcEncoder;
import io.github.rezeros.core.common.cache.CommonClientCache;
import io.github.rezeros.core.common.config.PropertiesBootstrap;
import io.github.rezeros.core.common.event.IRpcListenerLoader;
import io.github.rezeros.core.common.utils.CommonUtils;
import io.github.rezeros.core.filter.IClientFilter;
import io.github.rezeros.core.filter.client.ClientFilterChain;
import io.github.rezeros.core.proxy.ProxyFactory;
import io.github.rezeros.core.registry.AbstractRegister;
import io.github.rezeros.core.registry.RegistryConstant;
import io.github.rezeros.core.registry.RegistryService;
import io.github.rezeros.core.registry.URL;
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
import java.util.List;

import static io.github.rezeros.core.common.cache.CommonClientCache.*;
import static io.github.rezeros.core.common.constants.RpcConstants.DEFAULT_DECODE_CHAR;
import static io.github.rezeros.core.registry.RegistryConstant.PROVIDER_IPS;
import static io.github.rezeros.core.registry.RegistryConstant.SERVICE_PATH;

@Slf4j
@Data
public class Client {


    private static final EventLoopGroup clientGroup = new NioEventLoopGroup();

    private IRpcListenerLoader iRpcListenerLoader;

    private Bootstrap bootstrap = new Bootstrap();


    public static void main(String[] args) throws Throwable {
        Client client = new Client();
        RpcReference rpcReference = client.initClientApplication();
        RpcReferenceWrapper<DataService> rpcReferenceWrapper = new RpcReferenceWrapper<>();
        rpcReferenceWrapper.setAimClass(DataService.class);
        rpcReferenceWrapper.setGroup("default");
//        rpcReferenceWrapper.setUrl("192.168.43.227:9093");
        //在初始化之前必须要设置对应的上下文
        DataService dataService = rpcReference.get(rpcReferenceWrapper);
        client.doSubscribeService(DataService.class);
        ConnectionHandler.setBootstrap(client.getBootstrap());
        client.doConnectServer();
        client.startClient();
        for (int i = 0; i < 1000; i++) {
            try {
                String result = dataService.sendData("txxxxest");
                System.out.println(result);
                Thread.sleep(1000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void doConnectServer() {
        for (URL providerUrl : SUBSCRIBE_SERVICE_LIST) {
            List<String> providerIps = ABSTRACT_REGISTER.getProviderIps(providerUrl.getServiceName());
            for (String providerIp : providerIps) {
                try {
                    ConnectionHandler.connect(providerUrl.getServiceName(), providerIp);
                } catch (InterruptedException e) {
                    log.error("Client connect providerService {} failed. provider ip is: {}", providerUrl.getServiceName(), providerIp);
                    throw new RuntimeException(e);
                }
            }

            URL url = new URL();
            url.addParameter(SERVICE_PATH, providerUrl.getServiceName() + "/" + RegistryConstant.PROVIDER);
            url.addParameter(PROVIDER_IPS, JSON.toJSONString(providerIps));
            //客户端在此新增一个订阅的功能
            ABSTRACT_REGISTER.doAfterSubscribe(url);
        }
    }

    public void doSubscribeService(Class<?> serviceBean) {
        if (ABSTRACT_REGISTER == null) {
            try {
                //使用自定义的SPI机制去加载配置
                EXTENSION_LOADER.loadExtension(RegistryService.class);
                ABSTRACT_REGISTER = (AbstractRegister) EXTENSION_LOADER.getInstance(CLIENT_CONFIG.getRegisterType(), RegistryService.class);
            } catch (Exception e) {
                throw new RuntimeException("registryServiceType unKnow,error is ", e);
            }
        }

        URL url = new URL();
        url.setApplicationName(CLIENT_CONFIG.getApplicationName());
        url.setServiceName(serviceBean.getName());
        url.addParameter("host", CommonUtils.getIpAddress());
        ABSTRACT_REGISTER.subscribe(url);
    }

    public RpcReference initClientApplication() throws IOException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        CLIENT_CONFIG = PropertiesBootstrap.loadClientConfigFromLocal();
        bootstrap.group(clientGroup);
        bootstrap.channel(NioSocketChannel.class);
        bootstrap.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                log.info("init the consumer pipeline");
                ByteBuf delimiter = Unpooled.copiedBuffer(DEFAULT_DECODE_CHAR.getBytes());
                ch.pipeline().addLast(new DelimiterBasedFrameDecoder(CLIENT_CONFIG.getMaxServerRespDataSize(), delimiter));
                //管道中初始化一些逻辑，这里包含了上边所说的编解码器和客户端响应类
                ch.pipeline().addLast(new RpcEncoder());
                ch.pipeline().addLast(new RpcDecoder());
                ch.pipeline().addLast(new ClientHandler());
            }
        });
        //常规的链接netty服务端
        iRpcListenerLoader = new IRpcListenerLoader();
        iRpcListenerLoader.init();
        initClientConfig();
        EXTENSION_LOADER.loadExtension(ProxyFactory.class);
        return new RpcReference(EXTENSION_LOADER.getInstance(CLIENT_CONFIG.getProxyType(), ProxyFactory.class));
    }


    private void initClientConfig() throws IOException, IllegalAccessException, ClassNotFoundException, InstantiationException {
        //初始化路由策略
        EXTENSION_LOADER.loadExtension(IRouter.class);
        IROUTER = EXTENSION_LOADER.getInstance(CLIENT_CONFIG.getRouterStrategy(), IRouter.class);

        //初始化序列化框架
        EXTENSION_LOADER.loadExtension(SerializeFactory.class);

        CLIENT_SERIALIZE_FACTORY = EXTENSION_LOADER.getInstance(CLIENT_CONFIG.getClientSerialize(), SerializeFactory.class);

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
                    RpcProtocol rpcProtocol = new RpcProtocol(CLIENT_SERIALIZE_FACTORY.serialize(data));
                    ChannelFuture channelFuture = ConnectionHandler.getChannelFuture(data.getTargetServiceName());
                    //netty的通道负责发送数据给服务端
                    channelFuture.channel().writeAndFlush(rpcProtocol);
                } catch (Exception e) {
                    e.printStackTrace();
                    return;
                }
            }
        }
    }

}
