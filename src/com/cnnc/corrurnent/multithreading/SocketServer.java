package com.cnnc.corrurnent.multithreading;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by chenlou on 2018/8/27.
 */
public class SocketServer {

    public static void main(String[] args) throws IOException {
        // 监听指定的端口
        int port = 55533;
        ServerSocket server = new ServerSocket(port);
        // server将一直等待连接的到来
        System.out.println("server将一直等待连接的到来");

        ExecutorService executorService = Executors.newFixedThreadPool(100);

        while (true) {
            Socket socket = server.accept();

            Runnable runnable = ()-> {
                try {
                    // 建立好连接后，从socket中获取输入流，并建立缓冲区进行读取
                    InputStream inputStream = socket.getInputStream();
                    byte[] bytes = new byte[1024];
                    int len;
                    StringBuilder sb = new StringBuilder();
                    while ((len = inputStream.read(bytes)) != -1) {
                        // 注意指定编码格式，发送方和接收方一定要统一，建议使用UTF-8
                        sb.append(new String(bytes, 0, len, "UTF-8"));
                    }
                    System.out.println("get message from client: " + sb);
                    inputStream.close();
                    socket.close();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            };
            executorService.submit(runnable);
        }
    }
}
