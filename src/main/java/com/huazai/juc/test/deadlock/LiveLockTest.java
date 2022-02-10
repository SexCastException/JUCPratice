package com.huazai.juc.test.deadlock;

import lombok.extern.slf4j.Slf4j;

/**
 * 活锁出现在两个线程互相改变对方的结束条件，最后谁也无法结束。
 * 1、活锁问题使用synchronized无法解决问题，因为此时并没有共享资源，没有临界区代码
 * 2、活锁问题会让程序一直执行下去，相当于while(true); 此时无锁胜有锁
 * 3、活锁的线程状态还是RUNNABLE，而死锁的线程状态是BLOCK；
 */
@Slf4j
public class LiveLockTest {
    static volatile int count = 10;

    public static void main(String[] args) {
        new Thread(() -> {
            // 期望减到 0 退出循环，但t2线程打破t1线程的循环条件
            while (count > 0) {
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                count--;
                log.debug("count: {}", count);
            }
        }, "t1").start();
        new Thread(() -> {
            // 期望超过 20 退出循环，但t1线程打破t1线程的循环条件
            while (count < 20) {
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                count++;
                log.debug("count: {}", count);
            }
        }, "t2").start();
    }
}

