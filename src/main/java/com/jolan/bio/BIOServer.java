package com.jolan.bio;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BIOServer {
    public static void main(String[] args) throws Exception{

        //1.创建一个线程池
        //2.如果有客户端连接，就创建一个线程，与之通讯
        ExecutorService threadPool = Executors.newCachedThreadPool();

        //创建一个serverSocket
        ServerSocket serverSocket = new ServerSocket(7000);

        System.out.println("服务器启动了");

        while(true){
            //监听客户端连接
            Socket socket = serverSocket.accept();
            System.out.println("连接到一个客户端");

            //创建一个线程池通讯
            threadPool.execute(new Runnable() {
                @Override
                public void run() {
                    handler(socket);
                }
            });

        }
    }

    public static void handler(Socket socket){
        System.out.println("当前线程id：" + Thread.currentThread().getName());
        InputStream inputStream = null;
        try{
            byte[] bytes = new byte[1024];
            //通过socket获取输入流
            inputStream = socket.getInputStream();

            //循环读取数据
            while (true){
                System.out.println("当前线程id：" + Thread.currentThread().getName());

                int read = inputStream.read(bytes);
                if(read != -1){
                    System.out.println(new String(bytes, 0, read));
                }else{
                    break;
                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(inputStream != null){
                try {
                    inputStream.close();
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
