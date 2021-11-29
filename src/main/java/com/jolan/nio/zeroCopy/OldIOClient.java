package com.jolan.nio.zeroCopy;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class OldIOClient {
    public static void main(String[] args) throws Exception{
        Socket socket = new Socket("127.0.0.1", 7001);
        String fileName = "learnsewithpython.rar";
        FileInputStream fileInputStream = new FileInputStream(fileName);
        DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());

        byte[] bytes = new byte[4096];
        long readcount;
        int total = 0;

        long startTime = System.currentTimeMillis();

        while((readcount = fileInputStream.read(bytes)) > 0){
            total += readcount;
            dataOutputStream.write(bytes);
        }
        System.out.println("发送总字节数：" + total + ", 耗时：" + (System.currentTimeMillis() - startTime));

        dataOutputStream.close();
        socket.close();
        fileInputStream.close();
    }
}
