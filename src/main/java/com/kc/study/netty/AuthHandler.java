package com.kc.study.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class AuthHandler extends SimpleChannelInboundHandler<ByteBuf> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
        if(pass(msg)){
            ctx.pipeline().remove(this);
        }else {
            ctx.close();
        }
    }

    private boolean pass(ByteBuf msg) {
        return false;
    }
}
