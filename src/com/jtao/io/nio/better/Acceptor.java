package com.jtao.io.nio.better;

import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * <p>Title: Acceptor </p>
 *
 * <p>Description: </p>
 *
 * <p>Company: </p>
 *
 * <p>date: 2020/5/21  16:23 </p>
 *
 * @author: taojun
 */
public class Acceptor  implements Runnable{

    private final Selector selector;

    private final ServerSocketChannel serverSocketChannel;

    public Acceptor(ServerSocketChannel serverSocketChannel, Selector selector){
        this.serverSocketChannel = serverSocketChannel;
        this.selector = selector;
    }

    @Override
    public void run() {
        SocketChannel socketChannel;
        try {
                // ��ȡ�ͻ���
                socketChannel = serverSocketChannel.accept();
                System.out.println("�ͻ������ӳɹ�");
                if(socketChannel != null) {
                    new Handler(socketChannel, selector);
                }
        } catch (Exception e ) {
            e.printStackTrace();
        }
    }
}
