package cn.netty.c3;

/*
@author YG
@create 2022/10/30   19:59
*/

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;
@Slf4j
public class TestJdkFuture {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        // 1. 创建线程池
        ExecutorService service = Executors.newFixedThreadPool(2);
        // 2. 往线程池提交任务
        Future<Integer> future = service.submit(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                log.debug("执行计算");
                Thread.sleep(1000);
                return 50;
            }
        });
        // 3. 主线程通过 future 获取结果
        log.debug("等待结果");
        // submit非阻塞； get阻塞，直到计算的线程返回结果唤醒主线程
        log.debug("结果是:{}",future.get());
    }
}
