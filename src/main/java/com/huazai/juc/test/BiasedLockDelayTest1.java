package com.huazai.juc.test;

import lombok.extern.slf4j.Slf4j;
import org.openjdk.jol.info.ClassLayout;

import java.util.concurrent.TimeUnit;

@Slf4j
public class BiasedLockDelayTest1 {
    public static void main(String[] args) {
        Dog d = new Dog();
        Thread t1 = new Thread(() -> {
            synchronized (d) {
                log.debug(ClassLayout.parseInstance(d).toPrintable());
            }

            try {
                log.debug("t1睡眠1秒，让t2先拿到BiasedLockDelayTest1的类锁");
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            synchronized (BiasedLockDelayTest1.class) {
                log.debug("t1开始唤醒t2");
                BiasedLockDelayTest1.class.notify();
                log.debug("t1开始唤醒t2完毕");
            }
        }, "t1");
        t1.start();
        Thread t2 = new Thread(() -> {

            synchronized (BiasedLockDelayTest1.class) {
                try {
                    log.debug("t2开始等待，并释放锁资源");
                    BiasedLockDelayTest1.class.wait();
                    log.debug("t2被唤醒");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            log.debug(ClassLayout.parseInstance(d).toPrintable());
            synchronized (d) {
                log.debug("出现锁竞争，膨胀为重量级锁");
                log.debug(ClassLayout.parseInstance(d).toPrintable());
            }
            log.debug(ClassLayout.parseInstance(d).toPrintable());
        }, "t2");
        t2.start();
    }
}
