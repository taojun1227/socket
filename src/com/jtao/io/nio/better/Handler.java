package com.jtao.io.nio.better;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

/**
 * <p>Title: Handler </p>
 *
 * <p>Description: </p>
 *
 * <p>Company: </p>
 *
 * <p>date: 2020/5/21  17:24 </p>
 *
 * @author: taojun
 */
public class Handler implements Runnable{

    private final SocketChannel socketChannel;
    private final Selector selector;
    private SelectionKey selectionKey;

    private ByteBuffer readBuffer = ByteBuffer.allocate(1024);
    private ByteBuffer sendBuffer = ByteBuffer.allocate(1024);

    private final static int READ = 0;
    private final static int SEND = 1;
    private int status = READ;

    public Handler(SocketChannel socketChannel, Selector selector) throws IOException {
        this.socketChannel = socketChannel;
        this.selector = selector;

        socketChannel.configureBlocking(false);

        selectionKey = socketChannel.register(selector, SelectionKey.OP_READ);

        selectionKey.attach(this);

    }

    @Override
    public void run() {

        try {
            switch (status) {
                case READ:
                    read();
                    break;
                case SEND:
                    send();
                    break;
                default:
            }
        } catch (IOException e) {
            System.err.println("read或者send异常,报错信息" + e.getMessage());
            selectionKey.cancel();

            try {
                socketChannel.close();
            } catch (IOException ioException) {
                System.err.println("关闭通道异常，报错信息" + ioException.getMessage());
                ioException.printStackTrace();
            }
        } finally {
        }
    }

    private void read() throws IOException {
        if(selectionKey.isReadable()) {
            System.out.println("进入都事件");
            readBuffer.clear();
            int count = socketChannel.read(readBuffer);
            if(count >0) {
                System.out.println(String.format("收到客户端 %s 消息： %s",
                        socketChannel.getRemoteAddress(), new String(readBuffer.array())));
                status = SEND;
                // 修改为写事件
                selectionKey.interestOps(SelectionKey.OP_WRITE);
            } else {
                selectionKey.cancel();
                socketChannel.close();
                System.out.println("readʱ-------连接断开");
            }
        }
    }
    private void send() throws IOException {
        if(selectionKey.isWritable()) {
            System.out.println("进入写事件");
            sendBuffer.clear();
            sendBuffer.put("response ok".getBytes());
            sendBuffer.flip();
            int count = socketChannel.write(sendBuffer);
            status = READ;
            if(count < 0 ) {
                selectionKey.cancel();
                socketChannel.close();
                System.out.println("send-------连接断开");
            }
            status = READ;
            selectionKey.interestOps(SelectionKey.OP_READ);
        }
    }
}
