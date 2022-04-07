package com.huazai.juc.test.aqs;

import com.huazai.juc.test.utils.JucUtils;
import lombok.extern.slf4j.Slf4j;

/**
 * @author pyh
 * @datetime 2022/3/28 13:15
 * @description
 * @className MyLockTest
 */
@Slf4j
public class MyLockTest {
    public static void main(String[] args) throws InterruptedException {
        MyLock lock = new MyLock();
        Thread t1 = new Thread(() -> {
            log.debug(Thread.currentThread().getName() + "获取锁中...");
            lock.lock();
            try {
                log.debug(Thread.currentThread().getName() + "加锁成功");
                JucUtils.sleepSecond(1);
            } finally {
                log.debug(Thread.currentThread().getName() + "释放锁");
                lock.unlock();
            }
        }, "t1");

        Thread t2 = new Thread(() -> {
            log.debug(Thread.currentThread().getName() + "获取锁中...");
            lock.lock();
            try {
                log.debug(Thread.currentThread().getName() + "加锁成功");
                JucUtils.sleepSecond(1);
            } finally {
                log.debug(Thread.currentThread().getName() + "释放锁");
                lock.unlock();
            }
        }, "t2");
        t1.start();
        t2.start();

        t1.join();
        t2.join();
    }
}
