package io.github.rezeros.core.server;

import io.github.rezeros.protocol.RpcProtocol;
import io.netty.channel.ChannelHandlerContext;
import lombok.Data;

@Data
public class ServerChannelReadData {

    private RpcProtocol rpcProtocol;

    private ChannelHandlerContext channelHandlerContext;
}