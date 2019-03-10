package com.kc.study.netty;

import com.kc.study.handlers.*;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.AttributeKey;

public class ExceptionTest {
    public static void main(String[] args) throws Exception {
        EventLoopGroup bossGroup=new NioEventLoopGroup(1);
        EventLoopGroup workerGroup=new NioEventLoopGroup();
        try{
            ServerBootstrap b=new ServerBootstrap();
            b.group(bossGroup,workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childOption(ChannelOption.TCP_NODELAY,true)
                    .childAttr(AttributeKey.newInstance("childAttr"),"childAttrValue")
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new InBoundHandlerA());
                            ch.pipeline().addLast(new InBoundHandlerA());
                            ch.pipeline().addLast(new InBoundHandlerA());
                            ch.pipeline().addLast(new OutBoundHandlerA());
                            ch.pipeline().addLast(new OutBoundHandlerB());
                            ch.pipeline().addLast(new OutBoundHandlerC());
                            ch.pipeline().addLast(new ExceptionHandler());
                        }
                    });
            ChannelFuture f=b.bind(8888).sync();
            f.channel().closeFuture().sync();
        }finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
