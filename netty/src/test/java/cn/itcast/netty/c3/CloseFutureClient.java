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
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.Scanner;

@Slf4j
public class CloseFutureClient {
    public static void main(String[] args) throws InterruptedException {
        NioEventLoopGroup group = new NioEventLoopGroup();
        ChannelFuture channelFuture = new Bootstrap()
                .group(group)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new LoggingHandler(LogLevel.DEBUG));
                        ch.pipeline().addLast(new StringEncoder());
                    }
                })
                .connect(new InetSocketAddress("localhost", 8080));
        channelFuture.sync();
        Channel channel = channelFuture.channel();
        log.debug("{}",channel);
        new Thread(() -> {
            Scanner scanner = new Scanner(System.in);
            while (true) {
                String line = scanner.nextLine();
                if (line.equals("q")) {
                    channel.close();
//                    log.debug("处理关闭之后的操作");
                    break;
                }
                channel.writeAndFlush(line);
            }
        }, "input").start();

        // 对将来关闭后的操作进行处理，先获取closefuture对象，再处理
        // (1) 同步的方式
        ChannelFuture closeFuture = channel.closeFuture();
        log.debug("waiting close ...");
/*        closeFuture.sync(); // 主线程运行到这停止，直到channel关闭之后channel.close，才往下执行
        log.debug("处理关闭之后的操作");*/

        // (2) 异步的方式
        closeFuture.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                log.debug("处理关闭置之后的操作");
                // 优雅地关闭线程。 nio关闭后，group中还有一些线程没关闭，导致客户端无法关闭
                group.shutdownGracefully();
            }
        });
        log.debug("这是主线程");
    }
}
