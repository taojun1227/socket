package com.jtao.io.aio;

import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.CountDownLatch;

/**
 * <p>Title: AuthorizationController </p>
 *
 * <p>Description: </p>
 *
 * <p>Company: </p>
 *
 * <p>date: 2020/5/21 23:51 </p>
 *
 * @author: taojun
 */
public class AIOServer {
    public static void main(String[] args) {
        AsynchronousServerSocketChannel asynchronousServerSocketChannel= null;
        CountDownLatch countDownLatch = null;
        try{
            asynchronousServerSocketChannel = AsynchronousServerSocketChannel.open();
            asynchronousServerSocketChannel.bind(new InetSocketAddress(8080));
            System.out.println("服务的已启动");

            countDownLatch = new CountDownLatch(1);


            countDownLatch.await();

        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
