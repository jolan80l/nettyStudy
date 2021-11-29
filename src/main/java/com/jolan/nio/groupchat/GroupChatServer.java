package com.jolan.nio.groupchat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

public class GroupChatServer {
    //定义属性
    private Selector selector;
    private ServerSocketChannel listenChannel;
    private static final int PORT = 6667;

    public GroupChatServer() {
        try{
            //得到选择器
            selector = Selector.open();
            //获得ServerSocketChannel
            listenChannel = ServerSocketChannel.open();
            //绑定服务器端口
            listenChannel.socket().bind(new InetSocketAddress(PORT));
            //设置非阻塞模式
            listenChannel.configureBlocking(false);
            //将channel注册到Selector
            listenChannel.register(selector, SelectionKey.OP_ACCEPT);
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    //监听
    public void listen(){
        try{
            //循环处理
             while(true){
                 int count = selector.select(2000);
                 if(count > 0){
                    //遍历得到的SelectionKey
                     Set<SelectionKey> selectionKeys = selector.selectedKeys();
                     Iterator<SelectionKey> iterator = selectionKeys.iterator();
                     while(iterator.hasNext()){
                         SelectionKey selectionKey = iterator.next();
                         if(selectionKey.isAcceptable()){
                             SocketChannel socketChannel = listenChannel.accept();
                             socketChannel.configureBlocking(false);
                             //将该Channel注册到Selector
                             socketChannel.register(selector, SelectionKey.OP_READ);
                             //提示已上线
                             System.out.println(socketChannel.getRemoteAddress() + "上线");
                         }
                         if(selectionKey.isReadable()){
                            //通道发生read，即通道可读
                            read(selectionKey);
                         }
                         //把当前的key删除，防止会重复处理
                         iterator.remove();
                     }

                 }else{
                     //System.out.println("等待...");
                 }
             }
        }catch(Exception e){
            e.printStackTrace();
        }finally{

        }
    }

    //读取客户端消息
    private void read(SelectionKey selectionKey){
        //定义SocketChannel
        SocketChannel channel = null;
        try{
            //得到Channel
            channel = (SocketChannel) selectionKey.channel();
            //创建Buffer
            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

            int count = channel.read(byteBuffer);
            if(count > 0){
                //把缓冲区的数据转成字符串
                String msg = new String(byteBuffer.array());
                System.out.println("from客户端：" + msg);
                //向其他的客户端转发消息
                sendInfoToOtherClients(msg, channel);
            }
        }catch(IOException e){
            try {
                System.out.println(channel.getRemoteAddress() + "离线了...");
                //取消注册
                selectionKey.cancel();
                //关闭通道
                channel.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        }finally{

        }
    }

    //转发消息给其他的客户的函
    private void sendInfoToOtherClients(String msg, SocketChannel self) throws IOException{
        System.out.println("服务器转发消息中...");
        //遍历所有注册到Selector上的SocketChannel，并且排除自己
        System.out.println("注册到selector通道个数为：" + selector.selectedKeys().size());
        for(SelectionKey key : selector.keys()){
            //通过key取出socketChannel
            Channel targetChannel = key.channel();
            if(targetChannel instanceof SocketChannel && targetChannel != self){
                //转型
                SocketChannel dest = (SocketChannel)targetChannel;
                ByteBuffer buffer = ByteBuffer.wrap(msg.getBytes());
                //将buffer的数据写入通道
                dest.write(buffer);
            }
        }
    }

    public static void main(String[] args) {
        GroupChatServer chatServer = new GroupChatServer();
        chatServer.listen();
    }
}
