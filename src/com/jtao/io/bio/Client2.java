package com.jtao.io.bio;

import java.io.IOException;
import java.io.InputStream;
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
 * <p>date: 2020/5/21 21:46 </p>
 *
 * @author: taojun
 */
public class Client2 {

    public static void main(String[] args) {
        Socket socket = new Socket();
        OutputStream out = null;
        InputStream in = null;

        try {
            socket.connect(new InetSocketAddress("127.0.0.1", 8080));
            out = socket.getOutputStream();
            in = socket.getInputStream();
            Scanner scanner = new Scanner(System.in);
            byte [] b = new byte[1024];
            while (scanner.hasNext()){
                String message = scanner.next();
                out.write(message.getBytes());
                in.read(b);
                System.out.println(new String(b));
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
