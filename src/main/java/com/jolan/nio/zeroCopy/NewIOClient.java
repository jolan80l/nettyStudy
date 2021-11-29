package com.jolan.nio.zeroCopy;

import java.io.FileInputStream;
import java.net.InetSocketAddress;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;

public class NewIOClient {
    public static void main(String[] args) throws Exception{
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.connect(new InetSocketAddress("127.0.0.1", 7002));
        String fileName = "learnsewithpython.rar";

        //获取文件Channel
        FileChannel fileChannel = new FileInputStream(fileName).getChannel();

        long startTime = System.currentTimeMillis();

        //在Linux下一个transTo方法就可以完成传输
        //在windows下一次调用最多只能发送8M文件，如果大于8M就需要分段发送，需要注意传输位置
        long transferTo = fileChannel.transferTo(0, fileChannel.size(), socketChannel);
        System.out.println("发送的总的字节数是：" + transferTo + ", 总耗时时间为：" + (System.currentTimeMillis() - startTime));

        fileChannel.close();
    }
}
