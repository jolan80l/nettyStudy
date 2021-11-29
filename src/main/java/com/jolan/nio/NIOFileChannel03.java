package com.jolan.nio;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class NIOFileChannel03 {
    public static void main(String[] args) throws Exception{
        //定义输入输出流
        FileInputStream fileInputStream = new FileInputStream("file0301.txt");
        FileOutputStream fileOutputStream = new FileOutputStream("file0302.txt");

        //获取channel
        FileChannel inputStreamChannel = fileInputStream.getChannel();
        FileChannel fileOutputStreamChannel = fileOutputStream.getChannel();

        //定义Buffer
        ByteBuffer byteBuffer = ByteBuffer.allocate(512);

        while (true){
            //清空buffer，将buffer的关键的属性重置
            byteBuffer.clear();
            int read = inputStreamChannel.read(byteBuffer);
            if(read == -1){
                break;
            }
            //buffer反转
            byteBuffer.flip();
            fileOutputStreamChannel.write(byteBuffer);
        }

        fileInputStream.close();
        fileOutputStream.close();

    }
}
