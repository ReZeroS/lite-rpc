package io.github.rezeros.core.common;

import io.github.rezeros.core.common.constants.RpcConstants;
import io.github.rezeros.protocol.RpcProtocol;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RpcEncoder extends MessageToByteEncoder<RpcProtocol> {

    @Override
    protected void encode(ChannelHandlerContext ctx, RpcProtocol msg, ByteBuf out) throws Exception {
        // 写按顺序写即可
        log.info("rpc encoder");
        out.writeShort(msg.getMagicNumber());
        out.writeInt(msg.getContentLength());
        out.writeBytes(msg.getContent());
        out.writeBytes(RpcConstants.DEFAULT_DECODE_CHAR.getBytes());
    }
}