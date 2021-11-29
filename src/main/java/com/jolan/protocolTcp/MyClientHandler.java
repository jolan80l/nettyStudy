package com.jolan.protocolTcp;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.nio.charset.Charset;

public class MyClientHandler extends SimpleChannelInboundHandler<MessageProtocol> {

    private int count;
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        //使用客户端发送十条数据。今天天气好，适合晒太阳 + 编号
        for(int i = 0 ; i < 5 ; i++){
            String mes = "今天天气好，适合晒太阳";
            byte[] content = mes.getBytes(Charset.forName("utf-8"));
            int length = mes.getBytes(Charset.forName("utf-8")).length;
            //创建协议包
            MessageProtocol messageProtocol = new MessageProtocol();
            messageProtocol.setContent(content);
            messageProtocol.setLength(length);
            ctx.writeAndFlush(messageProtocol);
        }
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessageProtocol msg) throws Exception {
        int length = msg.getLength();
        byte[] content = msg.getContent();
        System.out.println("客户端接收到消息如下");
        System.out.println("客户端接收到的消息长度 = " + length);
        System.out.println("客户端接收到的消息内容 = " + new String(content, Charset.forName("utf-8")));
        System.out.println("客户端接收到消息次数 = " + (++this.count));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("异常信息" + cause.getMessage());
        ctx.close();
    }
}
