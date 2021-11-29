package com.jolan.nio;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class NIOFileChannel02 {
    public static void main(String[] args) throws Exception{

        //创建输输入流
        FileInputStream fileInputStream = new FileInputStream("file01.txt");

        //获取channel
        FileChannel fileChannel = fileInputStream.getChannel();

        //创建ByteBuffer
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

        //读取数据到Buffer
        fileChannel.read(byteBuffer);

        //输出文件中的内容
        System.out.println(new String(byteBuffer.array()));

        //关闭流
        fileInputStream.close();
    }
}
