package com.jtao.io.bio;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
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
public class Client {
    public static void main(String[] args) {
        Socket socket = new Socket();
        OutputStream out = null;

        try {
            socket.connect(new InetSocketAddress("127.0.0.1", 8080));
            out = socket.getOutputStream();
            Scanner scanner = new Scanner(System.in);
            while (scanner.hasNext()){
                String message = scanner.next();
                out.write(message.getBytes());
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                out.close();
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
