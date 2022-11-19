package io.github.rezeros.core.router;

import io.github.rezeros.core.registry.URL;

import java.nio.channels.Selector;

public interface IRouter {


    /**
     * 刷新路由数组
     */
    void refreshRouterArr(Selector selector);

    /**
     * 获取到请求的连接通道
     */
    ChannelFutureWrapper select(Selector selector);

    /**
     * 更新url权重
     */
    void updateWeight(URL url);


}
