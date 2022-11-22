package io.github.rezeros.core.registry.zookeeper;

import lombok.Data;

@Data
public class ProviderNodeInfo {

    private String applicationName;

    private String serviceName;

    private String address;

    private Integer weight;

    private String registryTime;

    private String group;

}
