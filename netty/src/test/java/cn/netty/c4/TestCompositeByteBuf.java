package cn.netty.c4;

/*
@author YG
@create 2022/11/1   22:30
*/

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

public class TestCompositeByteBuf {
    public static void main(String[] args) {
        ByteBuf buf1 = ByteBufAllocator.DEFAULT.buffer(5);
        buf1.writeBytes(new byte[]{1, 2, 3, 4, 5});

        ByteBuf buf2 = ByteBufAllocator.DEFAULT.buffer(5);
        buf2.writeBytes(new byte[]{6, 7, 8, 9, 10});

/*        System.out.println(ByteBufUtil.prettyHexDump(buf1));
        System.out.println(ByteBufUtil.prettyHexDump(buf2));*/

    }
}
