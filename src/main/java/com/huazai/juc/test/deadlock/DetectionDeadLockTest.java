package com.huazai.juc.test.deadlock;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * 定位死锁的方式：
 * 1、使用jconsole工具
 * 2、使用jps获取定位的进行UID，再执行 jstack UID
 *
 * 解决方案：
 * 1、改变加锁顺序，线程A和线程B分别都先获取a对象的monitor对象再获取b对象锁，缺点造成线程饥饿问题
 * 2、使用ReentrantLock
 */
@Slf4j
public class DetectionDeadLockTest {
    public static void main(String[] args) {
        Object A = new Object();
        Object B = new Object();
        Thread t1 = new Thread(() -> {
            synchronized (A) {
                log.debug("lock A");
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                synchronized (B) {
                    log.debug("lock B");
                    log.debug("操作...");
                }
            }
        }, "t1");
        Thread t2 = new Thread(() -> {
            synchronized (B) {
                log.debug("lock B");
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                synchronized (A) {
                    log.debug("lock A");
                }
            }
        }, "t2");

        t1.start();
        t2.start();
    }

}

