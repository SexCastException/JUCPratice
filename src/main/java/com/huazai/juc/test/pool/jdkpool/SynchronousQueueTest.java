package com.huazai.juc.test.pool.jdkpool;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.TimeUnit;

/**
 * SynchronousQueue 实现特点是，它没有容量，没有线程来取是放不进去的（一手交钱、一手交货）
 *
 * @author pyh
 * @datetime 2022/3/22 22:44
 * @description
 * @className SynchronousQueueTest
 */
@Slf4j
public class SynchronousQueueTest {
    public static void main(String[] args) throws InterruptedException {
        BlockingQueue<Integer> integerQueue = new SynchronousQueue<>();
//        BlockingQueue<Integer> integerQueue = new ArrayBlockingQueue(2);
        new Thread(() -> {
            try {
                log.debug("putting {} ", 1);
                integerQueue.put(1);
                log.debug("{} putted finish...", 1);
                log.debug("putting...{} ", 2);
                integerQueue.put(2);
                log.debug("{} putted finish...", 2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "t1").start();
        TimeUnit.SECONDS.sleep(1);
        new Thread(() -> {
            try {
                log.debug("taking {}", 1);
                integerQueue.take();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "t2").start();
        TimeUnit.SECONDS.sleep(1);
        new Thread(() -> {
            try {
                log.debug("taking {}", 2);
                integerQueue.take();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "t3").start();
    }
}
