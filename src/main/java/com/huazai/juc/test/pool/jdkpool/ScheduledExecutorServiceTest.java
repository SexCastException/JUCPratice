package com.huazai.juc.test.pool.jdkpool;

import com.huazai.juc.test.utils.JucUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * @author pyh
 * @datetime 2022/3/25 13:28
 * @description
 * @className ScheduledExecutorServiceTest
 */
@Slf4j
public class ScheduledExecutorServiceTest {
    public static void main(String[] args) {
        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(2);
        ScheduledFuture<?> schedule = scheduledExecutorService.schedule(() -> {
            log.debug("schedule");
            int i = 1 / 0;
        }, 1, TimeUnit.SECONDS);

        // 即使上一个线程抛出异常，不像Timer一样会影响下一个线程的执行
        schedule = scheduledExecutorService.schedule(() -> {
            log.debug("schedule1");
        }, 1, TimeUnit.SECONDS);

        /*
         在线程数充足的情况下，scheduleAtFixedRate每个循环执行的周期以线程开始的时间点开始，执行完上一个任务再开启下一个任务，
         所以该线程每个循环的周期为2s，线程数不充足的情况，还要加上等待空闲线程的时间
         */
        scheduledExecutorService.scheduleAtFixedRate(() -> {
            log.debug("scheduleAtFixedRate1");
            JucUtils.sleepSecond(2);
        }, 1, 1, TimeUnit.SECONDS);

        /*
         在线程数充足的情况下，scheduleWithFixedDelay每个循环执行的周期以线程结束的时间点开始，执行完上一个任务再开启下一个任务，
         所以该线程每个循环的周期为3s，线程数不充足的情况，还要加上等待空闲线程的时间
         */
        scheduledExecutorService.scheduleWithFixedDelay(() -> {
            log.debug("scheduleAtFixedRate2");
            JucUtils.sleepSecond(2);
        }, 1, 1, TimeUnit.SECONDS);
    }
}
