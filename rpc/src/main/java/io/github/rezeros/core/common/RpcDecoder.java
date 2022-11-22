package io.github.rezeros.core.common;

import io.github.rezeros.protocol.RpcProtocol;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

import static io.github.rezeros.core.common.constants.RpcConstants.MAGIC_NUMBER;

public class RpcDecoder extends ByteToMessageDecoder {

    /**
     * 协议的开头部分的标准长度
     */
    public final int BASE_LENGTH = 2 + 4;

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf byteBuf, List<Object> out) throws Exception {
        if (byteBuf.readableBytes() >= BASE_LENGTH) {
            //这里对应了RpcProtocol的魔数
            if (!(byteBuf.readShort() == MAGIC_NUMBER)) {
                    // 不是魔数开头，说明是非法的客户端发来的数据包
                ctx.close();
                return;
            }
            //这里对应了RpcProtocol对象的contentLength字段
            int length = byteBuf.readInt();
            //这里其实就是实际的RpcProtocol对象的content字段
            byte[] data = new byte[length];
            byteBuf.readBytes(data);
            RpcProtocol rpcProtocol = new RpcProtocol(data);
            out.add(rpcProtocol);
        }
    }
}