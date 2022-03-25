package com.huazai.juc.test.pool.jdkpool;

import com.huazai.juc.test.utils.JucUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * shutdown不会接受新的任务，但是执行完已经提交的线程
 *
 * @author pyh
 * @datetime 2022/3/23 22:47
 * @description
 * @className StopThreadExecutorTest
 */
@Slf4j
public class StopThreadExecutorTest {
    public static void main(String[] args) throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        executorService.submit(() -> {
            JucUtils.sleepSecond(2);
            log.debug("1");
            return "1";
        });
        log.debug("awaitTermination");
        // 等待线程池中的线程执行
        executorService.awaitTermination(5, TimeUnit.SECONDS);
        log.debug("shutDown");
        executorService.shutdown();
        // 已经终止的线程池不能在提交任务，如果再提交，则执行默认的拒绝策略
        /*executorService.submit(() -> {
            log.debug("2");
            return "2";
        });*/
    }
}
