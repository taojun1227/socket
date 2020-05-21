package com.jtao.io.nio.better;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * <p>Title: BetterNioServer </p>
 *
 * <p>Description: ???NIO</p>
 *
 * <p>Company: </p>
 *
 * <p>date: 2020/5/21  15:52 </p>
 *
 * @author: taojun
 */
public class BetterNioServer {

    private final Selector selector;
    private final ServerSocketChannel serverSocketChannel;

    public BetterNioServer(int port) throws IOException {
        this.selector = Selector.open();
        this.serverSocketChannel = ServerSocketChannel.open();
        // 绑定端口
        serverSocketChannel.bind(new InetSocketAddress(port));
        // 设置为非阻塞
        serverSocketChannel.configureBlocking(false);
        // 注册连接事件
       SelectionKey selectionKey =  serverSocketChannel.register(this.selector, SelectionKey.OP_ACCEPT);
       // 绑定事件处理器
        selectionKey.attach(new Acceptor(this.serverSocketChannel, this.selector));
    }

    public void run() {
        try {
            while (!Thread.interrupted()) {
                //获取 就绪的事件
                System.out.println("等待就绪事件");
                int count  =selector.select();
                System.out.println("找到就绪事件");
                // 获取就绪事件
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                System.out.println( "就绪事件个数"+selectionKeys.size());
                Iterator<SelectionKey> iterator = selectionKeys.iterator();
                while (iterator.hasNext()) {
                    dispatch(((SelectionKey) (iterator.next())));
                    iterator.remove();
                }

            }
        } catch (Exception e ) {}
    }

    void dispatch(SelectionKey key) {
        Runnable r = (Runnable) key.attachment();

        if(r != null) {
            r.run();
        }
    }


    public static void main(String[] args) throws IOException {
        BetterNioServer betterNioServer = new BetterNioServer(8080);

        betterNioServer.run();

    }


}
