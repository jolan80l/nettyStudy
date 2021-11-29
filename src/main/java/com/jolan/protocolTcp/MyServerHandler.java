package com.jolan.protocolTcp;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.nio.charset.Charset;
import java.util.Random;
import java.util.UUID;

public class MyServerHandler extends SimpleChannelInboundHandler<MessageProtocol> {
    private int count;
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessageProtocol msg) throws Exception {
        //接收到数据并处理
        int length = msg.getLength();
        byte[] content = msg.getContent();
        System.out.println("服务端接收到信息如下");
        System.out.println("length = " + length);
        System.out.println("content = " + new String(content, Charset.forName("utf-8")));

        System.out.println("服务器接收到消息包数量 = " + (++this.count));

        //回复消息
        String responseContent = UUID.randomUUID().toString();
        byte[] responseContentBytes = responseContent.getBytes("utf-8");
        int len = responseContentBytes.length;
        //构建协议包
        MessageProtocol messageProtocol = new MessageProtocol();
        messageProtocol.setLength(len);
        messageProtocol.setContent(responseContentBytes);
        ctx.writeAndFlush(messageProtocol);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.getCause();
        ctx.close();
    }
}
