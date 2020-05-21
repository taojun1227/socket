package com.jtao.io.nio;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * <p>Title: AuthorizationController </p>
 *
 * <p>Description: </p>
 *
 * <p>Company: </p>
 *
 * <p>date: 2020/5/21 22:55 </p>
 *
 * @author: taojun
 */
public class NIOClient {

    public static void main(String[] args) {
        Selector selector= null;
        SocketChannel socketChannel = null;
        try {
            socketChannel = SocketChannel.open();
            socketChannel.configureBlocking(false);
            selector = Selector.open();

            boolean mm = socketChannel.connect(new InetSocketAddress("127.0.0.1",8080));
            System.out.println("连接状态" + mm);
            socketChannel.register(selector, SelectionKey.OP_CONNECT);

            while(true) {
                selector.select();

                Set<SelectionKey> selectionKeys = selector.selectedKeys();

                Iterator<SelectionKey> iterator = selectionKeys.iterator();

                SelectionKey key = null;

                while(iterator.hasNext()){
                    key = iterator.next();
                    iterator.remove();

                    if(key.isConnectable()){
                        System.out.println("连接成功了吗");
                        SocketChannel sk = (SocketChannel) key.channel();
                        System.out.println("这是什么"+ sk.finishConnect());
                        ByteBuffer byteBuffer = ByteBuffer.allocate(1024).put("我不知道啊".getBytes());
                        byteBuffer.flip();
                        sk.write(byteBuffer);
                        sk.register(selector, SelectionKey.OP_READ);
                    }

                    if(key.isReadable()) {
                        System.out.println("读服务端返回消息");
                        SocketChannel sk = (SocketChannel) key.channel();
                        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                        sk.read(byteBuffer);
                        System.out.println("服务端返回消息：" +new String(byteBuffer.array()));
                    }

                    if(key.isWritable()){
                        System.out.println("写点什么好呢");
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
