package com.jolan.netty.buf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.util.CharsetUtil;

import java.nio.charset.Charset;

public class NettyBuf02 {
    public static void main(String[] args) {
        //创建ByteBuf
        ByteBuf byteBuf = Unpooled.copiedBuffer("hello world！世界你好！", CharsetUtil.UTF_8);

        //使用先关的api
        if(byteBuf.hasArray()){
            byte[] content = byteBuf.array();
            //将content转成字符串
            System.out.println(new String(content, Charset.forName("utf-8")));
            System.out.println("byteBuf类型 =  " + byteBuf);
            System.out.println(byteBuf.arrayOffset());//偏移量 = 0
            System.out.println(byteBuf.readerIndex());//readerindex = 0
            System.out.println(byteBuf.writerIndex());//wirterindex = 29 （英文和英文空格1个字节，中文和中文符号3个字节）
            System.out.println(byteBuf.capacity());//容量51
            System.out.println(byteBuf.readableBytes());//可读取的字节数 = 29
            for(int i = 0 ; i < byteBuf.readableBytes() ; i++){
                System.out.println((char)byteBuf.getByte(i));
            }
            //从第几个字符开始，读取指定的长度
            System.out.println(byteBuf.getCharSequence(0, 4, Charset.forName("utf-8")));


        }
    }
}
