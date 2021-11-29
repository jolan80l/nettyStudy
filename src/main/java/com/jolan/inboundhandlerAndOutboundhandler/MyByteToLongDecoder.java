package com.jolan.inboundhandlerAndOutboundhandler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

public class MyByteToLongDecoder extends ByteToMessageDecoder {
    /**
     * decode会根据接收的数据，被多次调用，直到确定没有新的元素被添加到list，或者ByteBuf没有更多的可读字节为止
     * 如果List out不为空，就会把list的内容传递给下一个InboundHandler处理，该处理器的方法也会被调用多次
     *
     * @param ctx  上下文
     * @param in   入站的ByteBuf
     * @param out  List集合，将解码后的数据传给下一个InboundHandler
     * @throws Exception
     */
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        System.out.println("MyByteToLongDecoder#decode被调用");
        //Long是八个字节，需要判断有8个字节才能读取
        if(in.readableBytes() >= 8){
            out.add(in.readLong());
        }
    }
}
