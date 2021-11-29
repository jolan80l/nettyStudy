package com.jolan.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class NIOClient {
    public static void main(String[] args) throws IOException {

        SocketChannel socketChannel = SocketChannel.open();
        //设置非阻塞模式
        socketChannel.configureBlocking(false);
        //设置远程ip和端口
        InetSocketAddress inetSocketAddress = new InetSocketAddress("127.0.0.1", 7000);

        if(!socketChannel.connect(inetSocketAddress)){
            while(!socketChannel.finishConnect()){
                System.out.println("因为连接需要时间，客户端不会阻塞，可以做其他工作...");
            }
        }
        //如果连接成功就发送数据
        String str = "Hello Jolan";
        //Wraps a byte array into a buffer.
        ByteBuffer byteBuffer = ByteBuffer.wrap(str.getBytes());
        //发送数据，将buffer中的数据写入到Channel
        socketChannel.write(byteBuffer);

        System.in.read();
    }
}
