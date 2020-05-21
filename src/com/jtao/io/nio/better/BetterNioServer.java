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
 * <p>Description: 优化NIO</p>
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
        // 绑定监听端口
        serverSocketChannel.bind(new InetSocketAddress(port));
        //设置为非阻塞
        serverSocketChannel.configureBlocking(false);
        //最开始给channel注册的事件都是accept，连接事件
       SelectionKey selectionKey =  serverSocketChannel.register(this.selector, SelectionKey.OP_ACCEPT);
       // 给这个事件绑定一个处理器
        selectionKey.attach(new Acceptor(this.serverSocketChannel, this.selector));
    }

    public void run() {
        try {
            while (!Thread.interrupted()) {
                //监听通道的就绪事件，如果没有就绪时间就阻塞,返回就事件的个数
                System.out.println("等待客户端上门");
                int count  =selector.select();
//                System.out.println("客户上门了");
                //拿到所有的就绪时间的集合
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                System.out.println( "就绪事件的大小："+selectionKeys.size());
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
