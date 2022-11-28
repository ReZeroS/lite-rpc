package io.github.rezeros.basic;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class NioSocketServer extends Thread {

    public static void main(String args[]) throws IOException {
        NioSocketServer server = new NioSocketServer();
        server.initServer();
        server.start();
    }

    Selector selector = null;
    ServerSocketChannel serverSocketChannel = null;


    private void initServer() throws IOException {
        selector = Selector.open();
        serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);
        serverSocketChannel.bind(new InetSocketAddress(1099));

        // 初始化时仅注册 Accept 事件
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

    }


    @Override
    public void run() {

        while(true) {
            try {
                int readyIoCount = selector.select();
                if (readyIoCount > 0) {
                    Set<SelectionKey> selectionKeys = selector.selectedKeys();

                    Iterator<SelectionKey> iterator = selectionKeys.iterator();

                    while (iterator.hasNext()) {
                        SelectionKey selectedKey = iterator.next();

                        iterator.remove();

                        if (selectedKey.isAcceptable()) {
                            accept(selectedKey);
                        }

                        if (selectedKey.isReadable()) {
                            read(selectedKey);

                            // 200M Buffer
//                            ByteBuffer buffer = ....
//                            // 注册写事件
//                            key.interestOps(key.interestOps() | SelectionKey.OP_WRITE);
//                            // 绑定Buffer
//                            key.attach(buffer);
                        }

                        if (selectedKey.isWritable()) {
//                            ByteBuffer buffer = (ByteBuffer) key.attachment();
//                            SocketChannel channel = (SocketChannel) key.channel();
//                            if (buffer.hasRemaining()) {
//                                channel.write(buffer)
//                            } else {
//                                // 发送完了就取消写事件，否则下次还会进入写事件分支（因为只要还可写，就会进入）
//                                key.interestOps(key.interestOps() & ~SelectionKey.OP_WRITE);
//                            }
                        }

                    }


                }


            } catch (IOException e) {
                try {
                    serverSocketChannel.close();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                throw new RuntimeException(e);
            }
        }


    }

    private void accept(SelectionKey selectedKey) throws IOException {
        // Accept 后 注册 read 事件
        ServerSocketChannel serverSocketChannel = (ServerSocketChannel)selectedKey.channel();
        SocketChannel socketChannel = serverSocketChannel.accept();
        System.out.println("conn is acceptable");
        socketChannel.configureBlocking(false);
        //将当前的channel交给selector对象监管，并且有selector对象管理它的读事件
        socketChannel.register(selector, SelectionKey.OP_READ);

    }


    private void read(SelectionKey selectedKey) {
        try {
            SocketChannel channel = (SocketChannel) selectedKey.channel();
            ByteBuffer byteBuffer = ByteBuffer.allocate(100);
            int len = channel.read(byteBuffer);
            if (len > 0) {
                byteBuffer.flip();
                byte[] byteArray = new byte[byteBuffer.limit()];
                byteBuffer.get(byteArray);
                System.out.println("NioSocketServer receive from client:" + new String(byteArray,0,len));
                selectedKey.interestOps(SelectionKey.OP_READ);
            }
        } catch (Exception e) {
            try {
                serverSocketChannel.close();
                selectedKey.cancel();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
        }
    }

    private void write(SelectionKey selectedKey) {
    }


}
