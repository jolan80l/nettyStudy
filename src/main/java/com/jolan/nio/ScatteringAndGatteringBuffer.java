package com.jolan.nio;
import sun.misc.BASE64Encoder;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Arrays;

/**
 * Scattering:将数组写入到buffer时，可以采用Buffer数组，依次写入
 * Gatterging:从Buffer读取数据是，可以采用Buffer数组，依次读取
 */
public class ScatteringAndGatteringBuffer {
    public static void main(String[] args) throws Exception{
        //使用ServerSocketChannel和SocketChannel
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();

        InetSocketAddress inetSocketAddress = new InetSocketAddress(7000);

        //绑定socket端口并启动
        serverSocketChannel.socket().bind(inetSocketAddress);

        //创建Buffer数组
        ByteBuffer[] byteBuffers = new ByteBuffer[2];

        byteBuffers[0] = ByteBuffer.allocate(5);
        byteBuffers[1] = ByteBuffer.allocate(3);

        //等待客户端连接
        SocketChannel socketChannel = serverSocketChannel.accept();

        //假定从客户端最多读取8个字节
        int messageLength = 8;
        //循环的读取
        while(true){
            int byteRead = 0;
            while(byteRead < messageLength){
                long read = socketChannel.read(byteBuffers);
                byteRead += read;//累计读取的字节数
                System.out.println("byteRead = " + byteRead);
                //使用流打印，查看当前bufer的position和limit
                Arrays.asList(byteBuffers).stream().map(buffer -> "position=" + buffer.position()
                    + ", limit = " + buffer.limit()).forEach(System.out::println);

                //这里怎么把ByteBuffer数组转成String？

                //将所有buffer反转
                Arrays.asList(byteBuffers).forEach(buffer -> buffer.flip());

                //将数据反显到客户端
                long bytewrite = 0;
                while(bytewrite < byteRead){
                    long write = socketChannel.write(byteBuffers);
                    bytewrite += write;
                }


                //将所有buffer复位
                Arrays.asList(byteBuffers).forEach(buffer -> buffer.clear());

                System.out.println("byteRead = " + byteRead + ", bytewrite = " + bytewrite + ", messageLength = " + messageLength);
            }
        }
    }
}
