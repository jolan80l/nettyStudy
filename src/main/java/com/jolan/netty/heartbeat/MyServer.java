package com.jolan.netty.heartbeat;

import com.jolan.netty.simple.NettyServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;

public class MyServer {
    public static void main(String[] args) {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try{
            //创建服务器端的启动对象，配置启动参数
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            //使用链式编程进行设置
            serverBootstrap.group(bossGroup, workerGroup)//设置两个线程组
                    .channel(NioServerSocketChannel.class)//使用NioServerSocketChannel作为服务器的通道实现
                    .handler(new LoggingHandler(LogLevel.INFO))//在bossGroup增加一个日志处理器
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            //加入一个netty提供的IdleStateHandler
                            /**
                             * 是netty提供的处理空闲状态的处理器
                             * int readerIdleTimeSeconds:多长时间没有读取，就会发送心跳检测包检测是否还是连接状态
                             * int writerIdleTimeSeconds:多长时间没有写操作，就会发送心跳检测包检测是否还是连接状态
                             * int allIdleTimeSeconds:多长时间没有读写操作，就会发送心跳检测包检测是否还是连接状态
                             * 当idleStateHandler触发后，就会传递给管道（pipeline）的下一个Handler去处理。通过调用下一个Handler的userEventTriggered，在该方法中去处理具体的事件
                             *
                             */
                            pipeline.addLast("idleStateHandler", new IdleStateHandler(3,5,7));
                            //加入对空闲检测进一步处理的Handler（自定义）
                            pipeline.addLast("myIdleStateHandler", new MyServerHandler());
                        }
                    })//给workerGroup的EventLoop对应的管道设置处理器
            ;
            //绑定一个端口并且同步
            ChannelFuture channelFuture = serverBootstrap.bind(8081).sync();
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
