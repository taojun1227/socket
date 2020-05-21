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
 * <p>Description: �Ż�NIO</p>
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
        // �󶨼����˿�
        serverSocketChannel.bind(new InetSocketAddress(port));
        //����Ϊ������
        serverSocketChannel.configureBlocking(false);
        //�ʼ��channelע����¼�����accept�������¼�
       SelectionKey selectionKey =  serverSocketChannel.register(this.selector, SelectionKey.OP_ACCEPT);
       // ������¼���һ��������
        selectionKey.attach(new Acceptor(this.serverSocketChannel, this.selector));
    }

    public void run() {
        try {
            while (!Thread.interrupted()) {
                //����ͨ���ľ����¼������û�о���ʱ�������,���ؾ��¼��ĸ���
                System.out.println("�ȴ��ͻ�������");
                int count  =selector.select();
//                System.out.println("�ͻ�������");
                //�õ����еľ���ʱ��ļ���
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                System.out.println( "�����¼��Ĵ�С��"+selectionKeys.size());
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
