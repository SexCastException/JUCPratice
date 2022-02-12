package com.huazai.juc.test.lock;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * ReentrantLock第二特性：可打断
 * ReentrantLock加锁过程相当于synchronized代码块，synchronized如果获取不到锁，则会一直阻塞，
 * 而ReentrantLock.lockInterruptibly()在获取锁过程中可以被中断，而不会一直堵塞下去。
 */
@Slf4j
public class ReentrantLockInterruptiblyTest {
    public static void main(String[] args) {
        ReentrantLock lock = new ReentrantLock();
        Thread t1 = new Thread(() -> {
            log.debug("t1线程正在获取锁...");
            try {
                lock.lockInterruptibly();
            } catch (InterruptedException e) {
                e.printStackTrace();
                log.debug("t1线程等锁的过程中被打断");
                return;
            }
            try {
                log.debug("t1线程成功获得了锁");
            } finally {
                lock.unlock();
            }
        }, "t1");

        lock.lock();
        log.debug("主线程获得了锁");
        t1.start();
        try {
            TimeUnit.SECONDS.sleep(1);
            t1.interrupt();
            log.debug("执行打断t1线程获取锁过程");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

}
