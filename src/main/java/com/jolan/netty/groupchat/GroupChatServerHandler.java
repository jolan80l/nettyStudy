package com.jolan.netty.groupchat;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.SocketHandler;

public class GroupChatServerHandler extends SimpleChannelInboundHandler<String> {
    //定义一个Channel组，管理所有的Channel
    //GlobalEventExecutor.INSTANCE 是全局的事件执行器，是一个单例
    private static ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    SimpleDateFormat simpleDateFormat =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * handlerAdded 表示连接建立后，第一个被执行
     * 这里将当前的Channel加入到channelGroup
     * @param ctx
     * @throws Exception
     */
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        //将该客户加入聊天的信息推送给其他在线的客户端
        //该方法会将所有channgeGroup中的channel遍历并发送消息
        channelGroup.writeAndFlush("【客户端】" + channel.remoteAddress() + "加入聊天组" + simpleDateFormat.format(new Date()) + "\n");
        //将当前channel加入聊天组
        channelGroup.add(channel);
    }

    /**
     * 表示channel处于活动状态
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ctx.channel().remoteAddress() + "上线了"  + simpleDateFormat.format(new Date()) + "\n");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ctx.channel().remoteAddress() + "离线了"  + simpleDateFormat.format(new Date()) + "\n");
    }

    /**
     * 断开连接出发
     * @param ctx
     * @throws Exception
     */
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        channelGroup.writeAndFlush("【客户端】" + channel.remoteAddress() + "离开聊天组"  + simpleDateFormat.format(new Date()) + "\n");
        System.out.println("当前channelGroup大小：" + channelGroup.size());
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        //先获取当前channel并读取数据
        Channel channel = ctx.channel();
        //遍历channelGroup，根据不同情况回送不同的消息
        channelGroup.forEach(ch -> {
            if(ch != channel){
                //不是当前channel，直接转发
                ch.writeAndFlush("【客户端】" + channel.remoteAddress() + "发送消息" + msg + "\n");
            }else{
                ch.writeAndFlush("【自己】" + "发送了消息：" + msg + "\n");
            }
        });
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
