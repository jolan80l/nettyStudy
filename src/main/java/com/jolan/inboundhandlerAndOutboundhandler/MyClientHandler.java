package com.jolan.inboundhandlerAndOutboundhandler;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

public class MyClientHandler extends SimpleChannelInboundHandler<Long> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Long msg) throws Exception {
        System.out.println("服务器的ip=" + ctx.channel().remoteAddress());
        System.out.println("收到服务器的消息:" + msg);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("MyClientHandler 发送数据");
        ctx.writeAndFlush(123456L);//发送一个Long

        //abcdabcdqwerqwer是16个字节
        //该处理器的前一个处理器是MyLongToByteEncoder
        //MyLongToByteEncoder父类是MessageToByteEncoder
        //MessageToByteEncoder会判断是否的write方法会判断需要出站的数据是不是Handler应该处理的类型，如果是则编码，否则跳过
        //因此在编写encoder时要注意传入的数据类型和要编码的处理类型要一致
        ctx.writeAndFlush(Unpooled.copiedBuffer("abcdabcdqwerqwer", CharsetUtil.UTF_8));
    }
}
