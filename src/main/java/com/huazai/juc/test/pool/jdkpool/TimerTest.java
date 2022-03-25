package com.huazai.juc.test.pool.jdkpool;

import lombok.extern.slf4j.Slf4j;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Timer.schedule参数：
 * delay：线程延迟多久才开始执行
 * period：每个周期间隔多久执一遍，如果没有此参数，表示则不会重复执行
 * Timer是串行执行的，如果有一个任务抛出异常了，则其他任务也会停止该任务。
 * <p>
 * 建议：ScheduledExecutorService使用替代Timer
 *
 * @author pyh
 * @datetime 2022/3/25 13:12
 * @description
 * @className TimerTest
 */
@Slf4j
public class TimerTest {
    public static void main(String[] args) {
        Timer timer = new Timer();

        log.debug("start task 1");
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                log.debug("task 1");
                int i = 10 / 0;
            }
        }, 1000L, 2000L);

        log.debug("start task 2");
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                log.debug("task 2");
            }
        }, 1000L, 1000L);
    }
}
