package com.huazai.juc.test;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.LockSupport;

@Slf4j
public class ThreadStateTest {
    public static void main(String[] args) throws InterruptedException {
        Thread thread1 = new Thread(() -> {

        });

        Thread thread2 = new Thread(() -> {
            while (true) {

            }
        });
        thread2.start();

        Thread thread3 = new Thread(() -> {
        });
        thread3.start();

        Thread thread4 = new Thread(() -> {
            try {
                thread2.join(); // WAITING
//                thread2.join(1000); // TIMED_WAITING
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        thread4.start();

        Thread thread5 = new Thread(() -> {
            try {
                synchronized (ThreadStateTest.class) {
//                    ThreadStateTest.class.wait();   // WAITING
//                    ThreadStateTest.class.wait(9999999);   // TIMED_WAITING
                    Thread.sleep(999999999);  // TIMED_WAITING
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        thread5.start();

        Thread.sleep(10);

        Thread thread6 = new Thread(() -> {
            try {
                // 线程5获取锁之后一直不释放，当前线程拿不到锁，处于BLOCKED状态
                synchronized (ThreadStateTest.class) {
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        thread6.start();

        Thread thread7 = new Thread(() -> {
            while (true) {
                Thread.yield();
//                log.debug("thread7线程调用yield方法之后状态：{}", Thread.currentThread().getState());
            }
        });
        thread7.start();

        Thread thread8 = new Thread(() -> {
//            LockSupport.park(); // WAITING
            // parkNanos(n)：让当前线程等待n纳秒后重新运行
//            LockSupport.parkNanos(999999999999999l);  // TIMED_WAITING
            // parkUntil(n)：让当前线程等待到具体某个时间点
            LockSupport.parkUntil(99999999 + System.currentTimeMillis());   // TIMED_WAITING
        });
        thread8.start();

        Thread.sleep(500);
        log.debug("thread1线程状态：{}", thread1.getState());
        log.debug("thread2线程状态：{}", thread2.getState());
        log.debug("thread3线程状态：{}", thread3.getState());
        log.debug("thread4线程状态：{}", thread4.getState());
        log.debug("thread5线程状态：{}", thread5.getState());
        log.debug("thread6线程状态：{}", thread6.getState());
        log.debug("thread7线程状态：{}", thread7.getState());
        log.debug("thread8线程状态：{}", thread8.getState());
    }
}
