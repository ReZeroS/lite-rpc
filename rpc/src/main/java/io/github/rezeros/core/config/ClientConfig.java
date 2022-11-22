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

}
