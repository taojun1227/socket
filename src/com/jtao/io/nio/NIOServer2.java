package com.jtao.io.nio;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
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
 * <p>date: 2020/5/20 23:05 </p>
 *
 * @author: taojun
 */
public class NIOServer2 {


    public static void main(String[] args) {
        try{
            //启动服务监听端口
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.bind(new InetSocketAddress(8080));

            //创建多路复用选择器  监听连接事件
            Selector acceptSelector = Selector.open();
            //设置未非阻塞方式
            serverSocketChannel.configureBlocking(false);
            //注册连接事件到 acceptSelector 上
            serverSocketChannel.register(acceptSelector, SelectionKey.OP_ACCEPT);

            // 轮询监听 selector
            while(true) {
                //调操作系统底层返回就绪的channel个数，阻塞的，必须要有一个就绪的channel才会往下走
                System.out.println("找举手的channel");
                int count = acceptSelector.select();
                System.out.println("找到了，有"+count + "channel连接进来");
                // 拿到就绪的事件集合
                Set<SelectionKey> selectionKeys = acceptSelector.selectedKeys();
                Iterator<SelectionKey> iterator = selectionKeys.iterator();
                while (iterator.hasNext()){
                    SelectionKey key = iterator.next();
                    // 拿出来用了后移除
                    iterator.remove();

                    if(key.isAcceptable()){
                        SocketChannel socketChannel = serverSocketChannel.accept();
                        System.out.println("建立连接");
                        socketChannel.configureBlocking(false);
                        // 注册read事件的channel到selector上
                        socketChannel.register(acceptSelector, SelectionKey.OP_READ);
                    } else if(key.isReadable()) {
                        SocketChannel socketChannel = (SocketChannel) key.channel();
                        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                        int len =  socketChannel.read(byteBuffer);
                        System.out.println("收到客户端消息："+ new String(byteBuffer.array(),0,len));
                        byteBuffer.clear();
                    }
                }

            }

        }catch (Exception e){}
    }
}
