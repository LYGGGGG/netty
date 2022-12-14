package cn.netty.c3;

/*
@author YG
@create 2022/10/29   1:26
*/

import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

@Slf4j
public class TestEventLoop {
    public static void main(String[] args) {
        // 1. 创建事件循环组
        EventLoopGroup group = new NioEventLoopGroup(2);

        // 2. 获取下一个事件循环对象
        System.out.println(group.next());

        // 3. 执行普通任务
      /*  group.next().submit(()->{
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.debug("ok");
        });*/

        // 4. 执行定时任务
        group.next().scheduleWithFixedDelay(()->{
            log.debug("ok");
        },0,1, TimeUnit.SECONDS);
//        log.debug("main");
    }
}
