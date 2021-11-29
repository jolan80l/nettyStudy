package com.jolan.nio.zeroCopy;

import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class NewIOServer {
    public static void main(String[] args) throws Exception{
        InetSocketAddress inetSocketAddress = new InetSocketAddress(7002);
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        ServerSocket serverSocket = serverSocketChannel.socket();
        serverSocket.bind(inetSocketAddress);
        ByteBuffer byteBuffer = ByteBuffer.allocate(4096);
        while(true){
            SocketChannel socketChannel = serverSocketChannel.accept();
            int readCount = 0;
            while(readCount != -1){
                try{
                    readCount = socketChannel.read(byteBuffer);
                }catch(Exception e){
                    e.printStackTrace();
                    break;
                }
                //position = 0, mark作废
                byteBuffer.rewind();
            }
        }

    }
}
