package com.jtao.io.bio;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

/**
 * <p>Title: AuthorizationController </p>
 *
 * <p>Description: </p>
 *
 * <p>Company: </p>
 *
 * <p>date: 2020/5/20 21:17 </p>
 *
 * @author: taojun
 */
public class Server {

    public static void main(String[] args) {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket();
            serverSocket.bind(new InetSocketAddress(8080));
            while(true) {
                System.out.println("等待连接");
                Socket socket = serverSocket.accept();
                System.out.println("收到客户端连接");

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        byte[] b = new byte[1024];
                        InputStream in = null;
                        while(true) {

                            try {
                                in = socket.getInputStream();
                                in.read(b);
                                System.out.println("收到客户端消息：" + new String(b));
                            } catch (IOException e) {
                                e.printStackTrace();
                            } finally {
                            }
                        }
                    }
                }).start();
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
        }

    }
}
