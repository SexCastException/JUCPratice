package com.huazai.juc.test.aqs;

import com.huazai.juc.test.utils.JucUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.StampedLock;

/**
 * StampedLock 不支持条件变量
 * StampedLock 不支持重入（读写，写写，写读）
 *
 * @author pyh
 * @datetime 2022/4/8 06:20
 * @description
 * @className StampedLockTest
 */
@Slf4j
public class StampedLockTest {
    public static void main(String[] args) throws InterruptedException {
        StampedLockTest stampedLockTest = new StampedLockTest(1);
        readAndRead(stampedLockTest);
//        writeAndRead(stampedLockTest);
//        readAndWrite(stampedLockTest);
//        writeAndWrite(stampedLockTest);
//        nonReentrant();
    }

    /**
     * StampedLock 不可重入测试
     */
    private static void nonReentrant() {
        StampedLock stampedLock = new StampedLock();
        long stamp1 = stampedLock.readLock();
        log.debug("stamp1：{}", stamp1);
        long stamp2 = stampedLock.writeLock();
        log.debug("stamp2：{}", stamp2);
        try {
            JucUtils.sleepSecond(1);
        } finally {
            stampedLock.unlockRead(stamp1);
            stampedLock.unlockRead(stamp2);
        }
    }

    /**
     * 写写测试
     *
     * @param stampedLockTest
     * @throws InterruptedException
     */
    private static void writeAndWrite(StampedLockTest stampedLockTest) throws InterruptedException {
        new Thread(() -> {
            stampedLockTest.write(2);
        }, "t1").start();

        Thread.sleep(500);

        new Thread(() -> {
            stampedLockTest.write(3);
        }, "t2").start();
    }

    /**
     * 写读测试
     *
     * @param stampedLockTest
     * @throws InterruptedException
     */
    private static void writeAndRead(StampedLockTest stampedLockTest) throws InterruptedException {
        new Thread(() -> {
            stampedLockTest.write(2);
        }, "t1").start();

        Thread.sleep(500);

        new Thread(() -> {
            stampedLockTest.read(150);
        }, "t2").start();
    }

    /**
     * 读写测试
     *
     * @param stampedLockTest
     * @throws InterruptedException
     */
    private static void readAndWrite(StampedLockTest stampedLockTest) throws InterruptedException {
        new Thread(() -> {
            stampedLockTest.read(150);
        }, "t1").start();

        Thread.sleep(500);

        new Thread(() -> {
            stampedLockTest.write(2);
        }, "t2").start();
    }

    /**
     * 读读测试
     *
     * @param stampedLockTest
     * @throws InterruptedException
     */
    private static void readAndRead(StampedLockTest stampedLockTest) throws InterruptedException {
        new Thread(() -> {
            stampedLockTest.read(100);
        }, "t1").start();

        Thread.sleep(500);

        new Thread(() -> {
            stampedLockTest.read(150);
        }, "t2").start();
    }

    private int data;

    private final StampedLock stampedLock = new StampedLock();

    public StampedLockTest(int data) {
        this.data = data;
    }

    public void write(int newData) {
        log.debug("ready to write new data:{}", newData);
        long stamp = stampedLock.writeLock();
        log.debug("write lock:{}", stamp);
        try {
            JucUtils.sleepSecond(2);
            log.debug("old data:{}", data);
            this.data = newData;
        } finally {
            stampedLock.unlockWrite(stamp);
            log.debug("release write lock:{}", stamp);
        }
    }

    public int read(long timeout) {
        long stamp = stampedLock.tryOptimisticRead();
        log.debug("optimistic read:{}", stamp);
        if (stampedLock.validate(stamp)) {
            log.debug("validate stamp success:{}", stamp);
            return data;
        }
        // 锁升级，乐观锁升级读锁
        log.debug("before add readLock data:{}", data);
        stamp = stampedLock.readLock();
        log.debug("updating to read lock:{}", stamp);
        try {
            JucUtils.sleepSecond(1);
            log.debug("return data:{}", data);
            return data;
        } finally {
            stampedLock.unlockRead(stamp);
        }
    }
}
