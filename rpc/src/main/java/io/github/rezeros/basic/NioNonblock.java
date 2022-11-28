package io.github.rezeros.basic;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class NioNonblock {

    public static void main(String[] args) throws IOException {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.bind(new InetSocketAddress(1099));
        SocketChannel socketChannel = serverSocketChannel.accept();
        socketChannel.configureBlocking(false);
        while (true) {
            ByteBuffer byteBuffer = ByteBuffer.allocate(100);
            int len = socketChannel.read(byteBuffer);
            if (len == -1) {
                System.out.println("No data");
            } else {
                byte[] byteArray = new byte[byteBuffer.limit()];
                byteBuffer.get(byteArray);
                System.out.println("Receive data: " + new String(byteArray));
            }
        }

    }
}
