package com.jolan.nio.zeroCopy;

import java.io.DataInputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class OldIOServer {
    public static void main(String[] args) throws Exception{
        ServerSocket serverSocket = new ServerSocket(7001);
        while(true){
            Socket socket = serverSocket.accept();
            DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
            try{
                byte[] byteArray = new byte[4096];
                long total = 0;
                while(true){
                    int read = dataInputStream.read(byteArray, 0, byteArray.length);
                    if(read == -1){
                        break;
                    }
                    total += read;
                }
                System.out.println("服务端读取了" + total + "字节数据");
            }catch(Exception e){
                e.printStackTrace();
            }finally{

            }
        }
    }
}
