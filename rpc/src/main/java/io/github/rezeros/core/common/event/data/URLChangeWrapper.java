package io.github.rezeros.core.common.event.data;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class URLChangeWrapper {

    private String serviceName;

    private List<String> providerUrl;

    //记录每个ip下边的url详细信息，包括权重，分组等
    private Map<String, String> nodeDataUrl;
}