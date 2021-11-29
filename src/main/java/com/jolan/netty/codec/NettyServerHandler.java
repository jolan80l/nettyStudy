package com.jolan.netty.codec;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

import java.util.concurrent.TimeUnit;

/**
 * 自定义的Handler需要继承Netty规定好的HandlerAdapter
 * 这时定义的Handler才能被Netty识别
 */
public class NettyServerHandler extends ChannelInboundHandlerAdapter {
    /**
     *
     * @param ctx 上下文对象，含有管道pipeline，通道Channel，地址
     * @param msg 客户端发送的数据，默认是Object格式，需要进行转换
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //        /*以下是第一版正常版本*/
//        System.out.println("server ctx = " + ctx);
//        //将msg转程ByteBuffer
//        ByteBuf buf = (ByteBuf)msg;
//        System.out.println("客户端发送消息是：" + buf.toString(CharsetUtil.UTF_8));
//        System.out.println("客户端地址：" +   ctx.channel().remoteAddress());


        /*以下代码是模拟耗时操作和解决方案*/
        /*模拟耗时操作*/
//        Thread.sleep(10 * 1000);
//        ctx.writeAndFlush(Unpooled.copiedBuffer("hello，客户端~服务端正在努力工作1", CharsetUtil.UTF_8));
//        System.out.println("go on do something...");

        /*耗时操作解决方案1：自定义任务*/
        ctx.channel().eventLoop().execute(new Runnable() {
            @Override
            public void run() {
                try{
                    Thread.sleep(10 * 1000);
                    ctx.writeAndFlush(Unpooled.copiedBuffer("hello，客户端~服务端正在努力工作2", CharsetUtil.UTF_8));
                }catch(Exception e){
                    e.printStackTrace();
                }
                System.out.println("go on do something...");
            }
        });

        ctx.channel().eventLoop().execute(new Runnable() {
            @Override
            public void run() {
                try{
                    Thread.sleep(20 * 1000);
                    ctx.writeAndFlush(Unpooled.copiedBuffer("hello，客户端~服务端正在努力工作3", CharsetUtil.UTF_8));
                }catch(Exception e){
                    e.printStackTrace();
                }
                System.out.println("go on do something...");
            }
        });


        /*耗时操作解决方案2：自定义定时任务*/
        ctx.channel().eventLoop().schedule(new Runnable() {
            @Override
            public void run() {
                try{
                    Thread.sleep(10 * 1000);
                    ctx.writeAndFlush(Unpooled.copiedBuffer("hello，客户端~服务端正在努力工作4", CharsetUtil.UTF_8));
                }catch(Exception e){
                    e.printStackTrace();
                }
                System.out.println("go on do something...");
            }
        }, 5, TimeUnit.SECONDS);


        System.out.println("go on ...");

    }

    /**
     * 数据读取完毕
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        //write+flush方法，将数据写入缓冲并刷新
        //一般需要对发送的数据进行编码
        ctx.writeAndFlush(Unpooled.copiedBuffer("hello，客户端~", CharsetUtil.UTF_8));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
