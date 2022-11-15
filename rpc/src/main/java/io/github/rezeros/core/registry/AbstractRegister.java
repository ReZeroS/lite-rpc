package io.github.rezeros.core.registry;

import java.util.List;

import static io.github.rezeros.cache.CommonClientCache.SUBSCRIBE_SERVICE_LIST;
import static io.github.rezeros.cache.CommonServerCache.PROVIDER_URL_SET;


/**
 * 这个抽象类主要的作用是对一些注册数据做统一的处理，假设日后需要考虑支持多种类型的注册中心
 * 例如 redis, etcd 之类的话，所有基础的记录操作都可以统一放在抽象类里实现
 */
public abstract class AbstractRegister implements RegistryService {


    @Override
    public void register(URL url) {
        PROVIDER_URL_SET.add(url);
    }

    @Override
    public void unRegister(URL url) {
        PROVIDER_URL_SET.remove(url);
    }

    @Override
    public void subscribe(URL url) {
        SUBSCRIBE_SERVICE_LIST.add(url.getServiceName());
    }

    /**
     * 留给子类扩展
     */
    public abstract void doAfterSubscribe(URL url);

    /**
     * 留给子类扩展
     */
    public abstract void doBeforeSubscribe(URL url);

    /**
     * 留给子类扩展
     */
    public abstract List<String> getProviderIps(String serviceName);


    @Override
    public void doUnSubscribe(URL url) {
        SUBSCRIBE_SERVICE_LIST.remove(url.getServiceName());
    }
}