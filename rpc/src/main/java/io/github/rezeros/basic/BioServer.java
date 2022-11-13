package io.github.rezeros.basic;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BioServer {

    private static ExecutorService executors = Executors.newFixedThreadPool(10);

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket();
        serverSocket.bind(new InetSocketAddress(1099));
        while (true) {
            // 堵塞点1
            Socket clientSocket = serverSocket.accept();

            executors.execute(() -> {

                try {
                    InputStream inputStream = clientSocket.getInputStream();
                    while (true) {
                        byte[] readBuffer = new byte[1024];
                        // 堵塞点2
                        int readStatus = inputStream.read(readBuffer);
                        if (readStatus != -1) {
                            OutputStream outputStream = clientSocket.getOutputStream();
                            outputStream.write(readBuffer);
                            outputStream.flush();
                        }
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }


            });
        }
    }
}