package com.jolan.netty.buf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class NettyBuf01 {
    public static void main(String[] args) {

        //Unpooled.buffer(10)创建了一个长度为10的字节数组byte[10]
        //在Netty的ByteBuf中，无需使用flip进行翻转，因为本身维护了readerindex和writerindex
        //0 - readerindex : 已经读取过的区域
        //readerindex - writerindex : 可以读取的区域
        //writerindex - capacity ： 可写的区域
        ByteBuf buffer = Unpooled.buffer(10);

        for(int i=0;i<10;i++){
            buffer.writeByte((byte)i);
        }
        System.out.println("capacity = " + buffer.capacity());
        for(int i=0;i<buffer.capacity();i++){
            buffer.readByte();
        }
    }
}
