package com.jolan.nio.groupchat;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Scanner;

public class GroupChatClient {
    //定义相关的属性
    private final String HOST = "127.0.0.1";
    private final int PORT = 6667;
    private Selector selector;
    private SocketChannel socketChannel;
    private String username;

    public GroupChatClient() throws IOException {
        selector = Selector.open();
        //连接服务器
        socketChannel = SocketChannel.open(new InetSocketAddress(HOST, PORT));
        //设置非阻塞
        socketChannel.configureBlocking(false);
        //将channel注册到Selector
        socketChannel.register(selector, SelectionKey.OP_READ);
        //得到username
        username = socketChannel.getLocalAddress().toString().substring(1);
        System.out.println(username + " is ok...");
    }

    //向服务器发送消息
    public void sendInfo(String info){
        info = username + " says: " + info;
        try{
            socketChannel.write(ByteBuffer.wrap(info.getBytes()));
        }catch(IOException e){
            e.printStackTrace();
        }finally{

        }
    }

    //读取从服务器端回复的消息
    public void readInfo(){
        try{
            int readChannels = selector.select();
            if(readChannels > 0){
                //有可用的通道
                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                while(iterator.hasNext()){
                    SelectionKey selectionKey = iterator.next();
                    if(selectionKey.isReadable()){
                        //得到相关的通道
                        SocketChannel src = (SocketChannel)selectionKey.channel();
                        //得到一个Buffer
                        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                        //读取
                        src.read(byteBuffer);
                        //把读到的缓冲区数据转成字符串
                        String msg = new String(byteBuffer.array());
                        System.out.println(msg.trim());
                        //删除当前的SelectionKey，防止重复操作
                        iterator.remove();
                    }
                }

            }
        }catch(Exception e){
            e.printStackTrace();
        }finally{

        }
    }

    public static void main(String[] args) throws Exception{
        GroupChatClient chatClient = new GroupChatClient();

        //启动一个线程，每隔3s读取一次服务端数据
        new Thread(() -> {
            while (true){
                chatClient.readInfo();
                try {
                    Thread.currentThread().sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        //发送数据给服务端
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()){
            String nextLine = scanner.nextLine();
            chatClient.sendInfo(nextLine);
        }
    }
}
