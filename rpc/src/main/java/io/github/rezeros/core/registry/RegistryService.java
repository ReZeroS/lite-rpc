package io.github.rezeros.core.registry;

public interface RegistryService {


    /**
     * 注册url 将irpc服务写入注册中心节点
     * 当出现网络抖动的时候需要进行适当的重试做法
     * 注册服务url的时候需要写入持久化文件中
     */
    void register(URL url);

    /**
     * 服务下线
     *
     * 持久化节点是无法进行服务下线操作的
     * 下线的服务必须保证url是完整匹配的
     * 移除持久化文件中的一些内容信息
     */
    void unRegister(URL url);

    /**
     * 消费方订阅服务
     */
    void subscribe(URL url);


    /**
     * 执行取消订阅内部的逻辑
     */
    void doUnSubscribe(URL url);
}