package com.jolan.netty.websocket;

import com.jolan.netty.heartbeat.MyServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.timeout.IdleStateHandler;

public class MyWebSocketServer {
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
                            //因为基于http协议，所以需要使用http的编码和解码器
                            pipeline.addLast(new HttpServerCodec());
                            //是以块的方式写
                            pipeline.addLast(new ChunkedWriteHandler());
                            /**
                             *  因为http在传输过程中是分段的，HttpObjectAggregator可以将多个段聚合起来
                             *  这就是当浏览器发送大量数据是，就会发出多次http请求
                             */
                            pipeline.addLast(new HttpObjectAggregator(8192));
                            /**
                             * 对于webSocket，它的数据是以帧的形式传递的
                             * WebSocketFrame有6个子类
                             * 浏览器发送请求时，ws://localhost:8081/xxx，表示请求的uri
                             * WebSocketServerProtocolHandler核心功能是将http协议升级为ws协议，保持长连接
                             *
                             */
                            pipeline.addLast(new WebSocketServerProtocolHandler("/hello"));
                            //自定义，处理业务逻辑
                            pipeline.addLast(new MyTextWebSocketFrameHandler());
                        }
                    })
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
