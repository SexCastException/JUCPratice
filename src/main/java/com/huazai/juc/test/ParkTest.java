package com.huazai.juc.test;

import com.huazai.juc.test.utils.JucUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.LockSupport;

/**
 * 打断 park 线程, 不会清空打断状态
 * 重置打断状态方法：
 * 1、Thread.interrupted()
 * 2、
 */
@Slf4j
public class ParkTest {
    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(() -> {
            log.debug("第一次park开始，打断状态：{}", Thread.currentThread().isInterrupted());
            // park方法会一直堵塞，直到打断标志为true，打断 park 线程, 不会清空打断状态
            LockSupport.park();
            log.debug("第一次park结束");
            log.debug("第二次park开始，打断状态：{}", Thread.currentThread().isInterrupted());
            LockSupport.park();
            log.debug("第二次park结束");
            log.debug("打断状态：{}", Thread.currentThread().isInterrupted());
            resetInterruptedBySleep();
//            resetInterrupted();
            log.debug("第三次park开始，打断状态：{}", Thread.currentThread().isInterrupted());
            /*
                如果是Thread.interrupted()重置打断标志，则park会一直阻塞，
                如果是sleep，wait等方法被打断导致打断标志重置，则即使打断状态为false，执行park方法也不会阻塞。
             */
            LockSupport.park();
            log.debug("第三次park结束...");
        }, "t1");
        t1.start();
        Thread.sleep(2000);
        log.debug("主线程开始打断t1线程");
        t1.interrupt();
    }

    /**
     * 通过打断阻塞方法（sleep，wait等）重置打断标志
     */
    private static void resetInterruptedBySleep() {
        log.debug("sleep，wait等阻塞api，不管是先阻塞后打断，还是先打断后阻塞，只要阻塞期间清空标志为true，都会抛出异常");
        JucUtils.sleepSecond(1);
        log.debug("阻塞被打断后，重置打断标志，打断状态：{}", Thread.currentThread().isInterrupted());
    }

    private static void resetInterrupted() {
        log.debug("Thread.interrupted()会重置打断标志，打断状态：{}", Thread.interrupted());   // Thread.interrupted()会重置打断标志
    }
}
