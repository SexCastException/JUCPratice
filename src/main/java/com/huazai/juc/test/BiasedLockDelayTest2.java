package com.huazai.juc.test;

import lombok.extern.slf4j.Slf4j;
import org.openjdk.jol.info.ClassLayout;

/**
 */
@Slf4j
public class BiasedLockDelayTest2 {

    public static void main(String[] args) throws InterruptedException {
        Dog d = new Dog();
        Thread t1 = new Thread(() -> {
            log.debug(ClassLayout.parseInstance(d).toPrintable());
            synchronized (d) {
                log.debug(ClassLayout.parseInstance(d).toPrintable());
                try {
                    d.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                // 被t2线程唤醒之后升级为重量级锁
                log.debug(ClassLayout.parseInstance(d).toPrintable());
            }
        }, "t1");
        t1.start();
        new Thread(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            synchronized (d) {
                log.debug("notify");
                d.notify();
            }
        }, "t2").start();
    }
}
