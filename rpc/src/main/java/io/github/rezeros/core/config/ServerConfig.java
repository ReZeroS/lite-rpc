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
}
