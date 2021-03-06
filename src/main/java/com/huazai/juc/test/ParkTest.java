package com.huazai.juc.test;

import com.huazai.juc.test.utils.JucUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.LockSupport;

/**
 * 打断 park 线程, 不会清空打断状态
 */
@Slf4j
public class ParkTest {
    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(() -> {
            log.debug("第一次park开始，打断状态：{}", Thread.currentThread().isInterrupted());
            // park方法会一直堵塞，直到打断标志为true，打断 park 线程, 不会清空打断状态
            LockSupport.park();
            log.debug("unpark...");
            log.debug("打断状态：{}", Thread.currentThread().isInterrupted());
//            JucUtils.sleepSecond(1);
//            log.debug("打断状态：{}", Thread.interrupted());   // Thread.interrupted()会重置打断标志，导致第二次park时候堵塞
            log.debug("第二次park开始，打断状态：{}", Thread.currentThread().isInterrupted());
            LockSupport.park();
            log.debug("第二次park结束...");
        }, "t1");
        t1.start();
        Thread.sleep(2000);
        log.debug("主线程开始打断t1线程");
        t1.interrupt();
    }
}
