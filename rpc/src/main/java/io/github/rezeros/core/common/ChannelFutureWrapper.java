package io.github.rezeros.core.common;

import io.netty.channel.ChannelFuture;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ChannelFutureWrapper {

    private ChannelFuture channelFuture;

    private String host;

    private Integer port;

    private Integer weight;

    private String group;

    public ChannelFutureWrapper(String host, Integer port,Integer weight) {
        this.host = host;
        this.port = port;
        this.weight = weight;
    }

}