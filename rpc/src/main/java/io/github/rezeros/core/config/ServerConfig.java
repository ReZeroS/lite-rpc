package io.github.rezeros.core.config;

import lombok.Data;

@Data
public class ServerConfig {

    private int serverPort;
    private int maxConnections;


    private String registerAddr;
    private String applicationName;

    /**
     * 限制服务端最大所能接受的数据包体积
     */
    private Integer maxServerRequestData;
    private String registerType;

    /**
     * 服务端业务线程数目
     */
    private Integer serverBizThreadNums;

    /**
     * 服务端接收队列的大小
     */
    private Integer serverQueueSize;

    /**
     * 服务端序列化方式 example: hession2,kryo,jdk,fastjson
     */
    private String serverSerialize;


}
