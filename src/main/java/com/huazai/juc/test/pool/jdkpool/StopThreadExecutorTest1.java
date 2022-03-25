package com.huazai.juc.test.pool.jdkpool;

import com.huazai.juc.test.utils.JucUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * shutdownNow返回还没开始的线程，并且中断已经开始执行的线程，并且不会接受新的任务
 *
 * @author pyh
 * @datetime 2022/3/25 11:24
 * @description
 * @className StopThreadExecutorTest1
 */
@Slf4j
public class StopThreadExecutorTest1 {
    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(1);
        executorService.submit(() -> {
            JucUtils.sleepSecond(5);
            log.debug("task1");
        });
        executorService.submit(() -> {
            JucUtils.sleepSecond(3);
            log.debug("task 2");
            return "hello world";
        });
        executorService.submit(() -> {
            JucUtils.sleepSecond(3);
            log.debug("task 2");
            return "hello world";
        });
        executorService.execute(() -> {
            JucUtils.sleepSecond(4);
            log.debug("task 3");
        });
        executorService.execute(() -> {
            JucUtils.sleepSecond(4);
            log.debug("task 3");
        });
        List<Runnable> runnables = executorService.shutdownNow();
        log.debug(runnables.toString());
    }
}
