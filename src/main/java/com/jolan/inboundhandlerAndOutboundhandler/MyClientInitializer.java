package com.jolan.inboundhandlerAndOutboundhandler;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

public class MyClientInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        //加入一个出站的handler，对数据进行编码
        pipeline.addLast(new MyLongToByteEncoder());

        //加一个入站handler，对数据解码
        pipeline.addLast(new MyByteToLongDecoder());

        //加入自定义Handler处理业务逻辑
        pipeline.addLast(new MyClientHandler());
    }
}
