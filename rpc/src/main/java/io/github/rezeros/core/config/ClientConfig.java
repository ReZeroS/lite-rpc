package io.github.rezeros.core.config;

import lombok.Data;

@Data
public class ClientConfig {


    private int port;

    private String serverAddr;

    private String applicationName;
    private String registerAddr;
    private String registerType;

    /**
     * 客户端发数据的超时时间
     */
    private Integer timeOut;

    /**
     * 代理类型 example: jdk,javassist
     */
    private String proxyType;


    /**
     * 客户端最大响应数据体积
     */
    private Integer maxServerRespDataSize;
    /**
     * 路由策略
     */
    private String routerStrategy;
    /**
     * 客户端序列化方式
     */
    private String clientSerialize;


}
