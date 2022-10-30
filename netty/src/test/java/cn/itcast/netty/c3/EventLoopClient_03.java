package cn.itcast.netty.c3;

/*
@author YG
@create 2022/10/28   11:47
*/

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.Date;

@Slf4j
public class EventLoopClient_03 {
    public static void main(String[] args) throws InterruptedException {
        // 2. 带有 Future, Promise 的类型都是和异步方法配套使用，用来处理结果
        ChannelFuture channelFuture = new Bootstrap()
                .group(new NioEventLoopGroup())
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<NioSocketChannel>() {
                    @Override // 在连接建立后，初始化
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new StringEncoder());
                    }
                })
                // 1. 连接到服务器
                // connect为异步非阻塞方法，main调用connect，真正执行connect的是nio线程
                .connect(new InetSocketAddress("localhost", 8080));
      /*  // 2.1 使用sync方法 同步 处理结果-->main线程等待
        channelFuture.sync(); // 阻塞当前线程main, 直到nio线程建立连接完毕
        Channel channel = channelFuture.channel();
        log.debug("{}", channel);
        channel.writeAndFlush(new Date() + ": hello,world!!");*/

        // 2.2 使用ChannelFuture的addListener(回调对象）  异步处理结果-->main线程不等待
        // 主线程main，通过addlistener，将回调对象ChannelFutureListener传递给nio线程
        // 当NIO线程连接建立好后，去执行回调对象的方法（这部分是nio执行，而不是main线程）
        channelFuture.addListener(new ChannelFutureListener() {
            @Override
            // NIO线程连接建立好之后，调用回调对象ChannelFutureListener的operationComplete方法
            public void operationComplete(ChannelFuture future) throws Exception {
                Channel channel = future.channel();
                log.debug("{}," + new Date(), channel);
                channel.writeAndFlush("hello, hi");
            }
        });

    }
}
