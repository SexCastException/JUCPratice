package com.huazai.juc.test;

import lombok.extern.slf4j.Slf4j;

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
                    ThreadStateTest.class.wait();   // WAITING
                    ThreadStateTest.class.wait(9999999);   // TIMED_WAITING
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
                synchronized (ThreadStateTest.class) {
                    Thread.sleep(999999999);
                }
            } catch (InterruptedException e) {
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

        Thread.sleep(500);
        log.debug("thread1线程状态：{}", thread1.getState());
        log.debug("thread2线程状态：{}", thread2.getState());
        log.debug("thread3线程状态：{}", thread3.getState());
        log.debug("thread4线程状态：{}", thread4.getState());
        log.debug("thread5线程状态：{}", thread5.getState());
        log.debug("thread6线程状态：{}", thread6.getState());
    }
}
