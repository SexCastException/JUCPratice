package com.huazai.juc.test.lock;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * ReentrantLock第三特性：锁超时
 * ReentrantLock加锁过程相当于synchronized代码块，synchronized如果获取不到锁，则会一直阻塞，
 * 而ReentrantLock.tryLock()先尝试获取锁（可以设置超时时长），获取失败之后做其他做操
 */
@Slf4j
public class ReentrantLockTimeOutTest {
    public static void main(String[] args) {
        ReentrantLock lock = new ReentrantLock();
        Thread t1 = new Thread(() -> {
            log.debug("t1线程尝试获取锁...");
            if (!lock.tryLock()) {
                log.debug("t1线程获取锁失败，结束运行");
                return;
            }
            try {
                log.debug("t1线程获得了锁");
            } finally {
                lock.unlock();
            }
        }, "t1");
        lock.lock();
        log.debug("主线程获得了锁");
        t1.start();
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }
}
