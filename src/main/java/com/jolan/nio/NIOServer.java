package com.jolan.nio;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

public class NIOServer {
    public static void main(String[] args) throws Exception{

        //创建一个ServerSocketChannel
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        //得到一个Selector对象
        Selector selector = Selector.open();
        //绑定7000端口
        serverSocketChannel.socket().bind(new InetSocketAddress(7000));
        //设置为非阻塞
        serverSocketChannel.configureBlocking(false);
        //把ServerSocketChannel注册到Selector，关心事件为OP_ACCEPT
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        //等待客户端连接
        while(true){
            //这里等待1S判断是否有事件
            if(selector.select(1000) == 0){
                System.out.println("服务器等待了1S，无关心事件");
                continue;
            }
            //如果返回大于0，获取SelectionKey集合
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            while(iterator.hasNext()){
                SelectionKey selectionKey = iterator.next();
                //如果是OP_ACCEPT，表示有新的客户端连接
                if(selectionKey.isAcceptable()){
                    //给该客户端生成一个SocketChannel
                    SocketChannel socketChannel = serverSocketChannel.accept();
                    System.out.println("客户端连接成功，生成了一个SocketChannel:" + socketChannel.hashCode());
                    socketChannel.configureBlocking(false);
                    //将当前的SocketChannel注册到selector，关注事件是读取事件，同时给socketChannel关联一个Buffer
                    socketChannel.register(selector, SelectionKey.OP_READ, ByteBuffer.allocate(1024));
                }else if(selectionKey.isReadable()){
                    //通过key反向获取对应的Channel
                    SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
                    //获取到该Channel关联的buffer
                    ByteBuffer buffer = (ByteBuffer)selectionKey.attachment();
                    socketChannel.read(buffer);
                    System.out.println("from客户端：" + new String(buffer.array()));
                }
            }
            //手动从集合中删除当前的SelectionKey，防止重复操作
            iterator.remove();
        }
    }
}
