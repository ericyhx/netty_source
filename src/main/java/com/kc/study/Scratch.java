package com.kc.study;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;

public class Scratch {
    public static void main(String[] args) {
        int page=1024*8;
        PooledByteBufAllocator allocator=PooledByteBufAllocator.DEFAULT;
        //page级别的内存分配：allocateNormal()
        allocator.directBuffer(2*page);
        //subPage级别的内存分配：allocateTiny()
        ByteBuf byteBuf=allocator.directBuffer(16);

        //内存回收
        byteBuf.release();
    }
}
