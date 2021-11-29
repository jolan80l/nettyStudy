package com.jolan.netty.codec;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class NettyServer {
    public static void main(String[] args) throws InterruptedException {
        //创建BossGroup和WorkerGroup
        //BossGroup仅处理连接请求，WorkerGroup与客户端进行业务处理
        //两个都是无限循环
        //bossGroup和workerGroup含有的子线程（NioEventLoop）的个数默认是cpu的核数 * 2
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try{
            //创建服务器端的启动对象，配置启动参数
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            //使用链式编程进行设置
            serverBootstrap.group(bossGroup, workerGroup)//设置两个线程组
                    .channel(NioServerSocketChannel.class)//使用NioServerSocketChannel作为服务器的通道实现
                    .option(ChannelOption.SO_BACKLOG, 128)//设置线程队列等待连接的个数
                    .childOption(ChannelOption.SO_KEEPALIVE, true)//只是保持连接活动状态
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        //给pipeline设置处理器
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new NettyServerHandler());
                        }
                    })//给workerGroup的EventLoop对应的管道设置处理器
            ;
            System.out.println("服务器is ready");
            //绑定一个端口并且同步
            ChannelFuture channelFuture = serverBootstrap.bind(6668).sync();
            //为ChannelFuture添加监听器
            channelFuture.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    if(future.isSuccess()){
                        System.out.println("端口绑定成功");
                    }else{
                        System.out.println("端口绑定失败");
                    }
                }
            });
            //对关闭通道进行监听
            channelFuture.channel().closeFuture().sync();
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }

    }
}
