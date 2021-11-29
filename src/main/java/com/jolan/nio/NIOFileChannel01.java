package com.jolan.nio;

import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class NIOFileChannel01 {
    public static void main(String[] args) throws Exception{

        String str = "hello FileChannel01";

        FileOutputStream fileOutputStream = new FileOutputStream("file01.txt");

        //从输出流获取channel
        FileChannel fileChannel = fileOutputStream.getChannel();

        //创建ByteBuffer缓冲区
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

        //将数据放入buffer
        byteBuffer.put(str.getBytes());

        //buffer反转
        byteBuffer.flip();

        //将byteBuffer数据写入到channel
        fileChannel.write(byteBuffer);

        //关闭流
        fileOutputStream.close();

    }
}
