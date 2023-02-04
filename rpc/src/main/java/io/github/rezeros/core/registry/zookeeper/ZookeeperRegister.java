package io.github.rezeros.core.registry.zookeeper;

import com.alibaba.fastjson.JSON;
import io.github.rezeros.core.common.event.IRpcEvent;
import io.github.rezeros.core.common.event.IRpcListenerLoader;
import io.github.rezeros.core.common.event.IRpcNodeChangeEvent;
import io.github.rezeros.core.common.event.IRpcUpdateEvent;
import io.github.rezeros.core.common.event.data.URLChangeWrapper;
import io.github.rezeros.core.registry.AbstractRegister;
import io.github.rezeros.core.registry.RegistryConstant;
import io.github.rezeros.core.registry.RegistryService;
import io.github.rezeros.core.registry.URL;
import io.github.rezeros.test.DataService;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;

import java.util.List;

import static io.github.rezeros.core.common.cache.CommonClientCache.CLIENT_CONFIG;
import static io.github.rezeros.core.common.cache.CommonServerCache.SERVER_CONFIG;

public class ZookeeperRegister extends AbstractRegister implements RegistryService {

    private final AbstractZookeeperClient zkClient;

    private final String ROOT = "/irpc";

    private String getProviderPath(URL url) {
        return ROOT + "/" + url.getServiceName() + "/provider/" + url.getParameters().get("host") + ":" + url.getParameters().get("port");
    }

    private String getConsumerPath(URL url) {
        return ROOT + "/" + url.getServiceName() + "/consumer/" + url.getApplicationName() + ":" + url.getParameters().get("host")+":";
    }

    public ZookeeperRegister() {
        String registryAddr = CLIENT_CONFIG != null? CLIENT_CONFIG.getRegisterAddr(): SERVER_CONFIG.getRegisterAddr();
        this.zkClient = new CuratorZookeeperClient(registryAddr);
    }


    public ZookeeperRegister(String address) {
        this.zkClient = new CuratorZookeeperClient(address);
    }


    @Override
    public List<String> getProviderIps(String serviceName) {
        return this.zkClient.getChildrenData(ROOT + "/" + serviceName + "/provider");
    }


    @Override
    public void register(URL url) {
        if (!this.zkClient.existNode(ROOT)) {
            zkClient.createPersistentData(ROOT, "");
        }
        String urlStr = URL.buildProviderUrlStr(url);
        if (!zkClient.existNode(getProviderPath(url))) {
            zkClient.createTemporaryData(getProviderPath(url), urlStr);
        } else {
            zkClient.deleteNode(getProviderPath(url));
            zkClient.createTemporaryData(getProviderPath(url), urlStr);
        }
        super.register(url);
    }

    @Override
    public void unRegister(URL url) {
        zkClient.deleteNode(getProviderPath(url));
        super.unRegister(url);
    }

    @Override
    public void subscribe(URL url) {
        if (!this.zkClient.existNode(ROOT)) {
            zkClient.createPersistentData(ROOT, "");
        }
        String urlStr = URL.buildConsumerUrlStr(url);
        if (!zkClient.existNode(getConsumerPath(url))) {
            zkClient.createTemporarySeqData(getConsumerPath(url), urlStr);
        } else {
            zkClient.deleteNode(getConsumerPath(url));
            zkClient.createTemporarySeqData(getConsumerPath(url), urlStr);
        }
        super.subscribe(url);
    }

    @Override
    public void doAfterSubscribe(URL url) {
        //监听是否有新的服务注册
        String servicePath = url.getParameter(RegistryConstant.SERVICE_PATH);
        String newServerNodePath = ROOT + "/" + servicePath;
        watchChildNodeData(newServerNodePath);
        String providerIpStrJson = url.getParameter(RegistryConstant.PROVIDER_IPS);
        List<String> providerIpList = JSON.parseArray(providerIpStrJson, String.class);
        for (String providerIp : providerIpList) {
            //启动环节会触发订阅订阅节点详情地址为：/irpc/com.sise.test.UserService/provider/192.11.11.101:9090
            this.watchNodeDataChange(ROOT + "/" + servicePath + "/" + providerIp);
        }
    }
    public void watchNodeDataChange(String newServerNodePath) {
        zkClient.watchNodeData(newServerNodePath, watchedEvent -> {
            String path = watchedEvent.getPath();
            System.out.println("[watchNodeDataChange] 监听到zk节点下的" + path + "节点数据发生变更");
            String nodeData = zkClient.getNodeData(path);
            ProviderNodeInfo providerNodeInfo = URL.buildURLFromUrlStr(nodeData);
            IRpcEvent iRpcEvent = new IRpcNodeChangeEvent(providerNodeInfo);
            IRpcListenerLoader.sendEvent(iRpcEvent);
            watchNodeDataChange(newServerNodePath);
        });
    }

    public void watchChildNodeData(String newServerNodePath){
        zkClient.watchChildNodeData(newServerNodePath, watchedEvent -> {
            System.out.println(watchedEvent);
            String path = watchedEvent.getPath();
            List<String> childrenDataList = zkClient.getChildrenData(path);
            URLChangeWrapper urlChangeWrapper = new URLChangeWrapper();
            urlChangeWrapper.setProviderUrl(childrenDataList);
            urlChangeWrapper.setServiceName(path.split("/")[2]);
            //自定义的一套事件监听组件
            IRpcEvent iRpcEvent = new IRpcUpdateEvent(urlChangeWrapper);
            IRpcListenerLoader.sendEvent(iRpcEvent);
            //收到回调之后在注册一次监听，这样能保证一直都收到消息
            watchChildNodeData(path);
        });
    }

    @Override
    public void doBeforeSubscribe(URL url) {

    }

    @Override
    public void doUnSubscribe(URL url) {
        this.zkClient.deleteNode(getConsumerPath(url));
        super.doUnSubscribe(url);
    }

    public static void main(String[] args) throws InterruptedException {
        ZookeeperRegister zookeeperRegister = new ZookeeperRegister("localhost:2181");
        List<String> urls = zookeeperRegister.getProviderIps(DataService.class.getName());
        System.out.println(urls);
        Thread.sleep(2000000);
    }
}