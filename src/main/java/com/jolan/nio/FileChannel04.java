package com.jolan.nio;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;

public class FileChannel04 {
    public static void main(String[] args) throws Exception{
        //创建相关流
        FileInputStream fileInputStream = new FileInputStream("from.jpg");
        FileOutputStream fileOutputStream = new FileOutputStream("to.jpg");

        //获取各个流对应的channel
        FileChannel sourceChannel = fileInputStream.getChannel();
        FileChannel destChannel = fileOutputStream.getChannel();

        //使用transFrom完成copy
        destChannel.transferFrom(sourceChannel, 0, sourceChannel.size());

        //关闭通道和流
        sourceChannel.close();
        destChannel.close();

        fileInputStream.close();
        fileOutputStream.close();


    }
}
