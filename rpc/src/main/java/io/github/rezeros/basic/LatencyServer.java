package io.github.rezeros.basic;

import io.github.rezeros.core.common.RpcDecoder;
import io.github.rezeros.core.common.RpcEncoder;
import io.github.rezeros.core.common.ServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.Timer;

import static io.github.rezeros.core.common.constants.RpcConstants.DEFAULT_DECODE_CHAR;

@Slf4j
public class LatencyServer {


    public static void main(String[] args) {
        Timer gatewayTimer = new Timer();

//        gateWayTimer.schedule(timerTask, new Date(),  1888)
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();
        ServerBootstrap b = new ServerBootstrap();
        b.group(bossGroup, workerGroup)
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .childOption(ChannelOption.TCP_NODELAY,  true)
                .channel (NioServerSocketChannel.class)
                .handler(new LoggingHandler(LogLevel.INFO))
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {

                    }
                });
        ChannelFuture bind = b.bind(10888);
        log.info("采集工作站\"+this.WsID＋“启动端口鄉定成功，端口号：" + 10888);
    }


}
