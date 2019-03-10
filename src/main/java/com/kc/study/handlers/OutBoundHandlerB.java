package com.kc.study.handlers;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;

import java.util.concurrent.TimeUnit;

public class OutBoundHandlerB extends ChannelOutboundHandlerAdapter {
    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        System.out.println("OutboundHandlerB:"+msg);
        ctx.write(msg,promise);
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
//        ctx.executor().schedule(()->{
//            //按照添加顺序从tail节点反向传播
//            ctx.channel().write("hello world");
//            //从当前节点开始反向传播
////            ctx.write("hello world");
//        },3, TimeUnit.SECONDS);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("OutBoundHandlerB exception");
        ctx.fireExceptionCaught(cause);
    }
}
