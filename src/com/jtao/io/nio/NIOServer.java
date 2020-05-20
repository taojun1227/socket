package com.jtao.io.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>Title: AuthorizationController </p>
 *
 * <p>Description: </p>
 *
 * <p>Company: </p>
 *
 * <p>date: 2020/5/20 22:09 </p>
 *
 * @author: taojun
 */
public class NIOServer {

    public static void main(String[] args) throws IOException, InterruptedException {
        List<SocketChannel> list = new ArrayList<>();
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        Selector acceptSelector = Selector.open();

        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.socket().bind(new InetSocketAddress(8080 ));
        serverSocketChannel.configureBlocking(false);
        System.out.println("NIOServer 启动");
        while(true){
            SocketChannel socketChannel = serverSocketChannel.accept();
            Thread.sleep(3000);
            if(socketChannel == null){
                System.out.println("未收到连接");
                for(SocketChannel channel : list){
                    int len = channel.read(byteBuffer);
                    byteBuffer.flip();
                    System.out.println("读取到消息："+ new String(byteBuffer.array(),0,len));
                    byteBuffer.clear();
                }
            } else {
                System.out.println("收到一个连接");
                //设置未非阻塞
                socketChannel.configureBlocking(false);
                list.add(socketChannel);
            }

        }
    }

}
