package com.huazai.juc.test;

/**
 * 1、调用 sleep 会让当前线程从 Running 进入 Timed Waiting 状态（阻塞）
 * 2、调用 yield 会让当前线程从 Running 进入 Runnable 就绪状态，然后调度执行其它线程
 */
public class YieldTest {
    public static void main(String[] args) {
        Runnable task1 = () -> {
            int count = 0;
            for (; ; ) {
                Thread.yield();
                System.out.println("---->1 " + count++);
            }
        };
        Runnable task2 = () -> {
            int count = 0;
            for (; ; ) {
//                Thread.yield();
                System.out.println(" ---->2 " + count++);
            }
        };
        Thread t1 = new Thread(task1, "t1");
        Thread t2 = new Thread(task2, "t2");
        t1.setPriority(Thread.MIN_PRIORITY);
        t2.setPriority(Thread.MAX_PRIORITY);
        t1.start();
        t2.start();
    }
}
