package cn.itcast.netty.c1;

/*
@author YG
@create 2022/10/27   21:52
*/

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;

public class HelloServer {
    public static void main(String[] args) {
        // 1. 启动器，负责组装下边的 netty 组件，作为服务器启动程序
        new ServerBootstrap()
                // 2. 线程池+选择器 BossEventLoop WorkerEventLoop(selector,thread)
                .group(new NioEventLoopGroup())
                // 3. 选择服务器的 ServerSocketChannel 实现
                .channel(NioServerSocketChannel.class)
                // 4. 添加子处理器，child 可理解为 worker
                .childHandler(
                        // 5. channel代表和客户端进行数据读写的通道，initializer初始化，负责添加其他handler
                        new ChannelInitializer<NioSocketChannel>() {
                            @Override
                            // 6. 添加具体的handler
                            protected void initChannel(NioSocketChannel ch) {
                                // SocketChannel 的处理器，解码 ByteBuf => String
                                ch.pipeline().addLast(new StringDecoder());
                                // 自定义handler，SocketChannel 的业务处理器，使用上一个处理器的处理结果
                                ch.pipeline().addLast(new SimpleChannelInboundHandler<String>() {
                                    @Override
                                    protected void channelRead0(ChannelHandlerContext ctx, String msg) {
                                        System.out.println(msg);
                                    }
                                });
                            }
                        })
                .bind(8080);

    }
}
