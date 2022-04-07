package com.huazai.juc.test.aqs;

import com.huazai.juc.test.utils.JucUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 读写互斥，写写互斥，读读不互斥
 *
 * @author pyh
 * @datetime 2022/3/31 10:55
 * @description
 * @className ReentrantReadWriteLockTest
 */
@Slf4j
public class ReentrantReadWriteLockTest {
    Object data = "hello world";

    ReentrantReadWriteLock reentrantReadWriteLock = new ReentrantReadWriteLock();

    ReentrantReadWriteLock.ReadLock readLock = reentrantReadWriteLock.readLock();

    ReentrantReadWriteLock.WriteLock writeLock = reentrantReadWriteLock.writeLock();

    public static void main(String[] args) throws InterruptedException {
        ReentrantReadWriteLockTest reentrantReadWriteLockTest = new ReentrantReadWriteLockTest();
        readAndWriteTest(reentrantReadWriteLockTest);
        readAndReadTest(reentrantReadWriteLockTest);
        writeAndWriteTest(reentrantReadWriteLockTest);


    }

    private static void writeAndWriteTest(ReentrantReadWriteLockTest reentrantReadWriteLockTest) throws InterruptedException {
        new Thread(() -> {
            reentrantReadWriteLockTest.write("你好🇨🇳");
        }, "t1").start();

        Thread.sleep(100);

        new Thread(() -> {
            reentrantReadWriteLockTest.write("你好China");
        }, "t2").start();
    }

    private static void readAndReadTest(ReentrantReadWriteLockTest reentrantReadWriteLockTest) throws InterruptedException {
        new Thread(() -> {
            reentrantReadWriteLockTest.read();
        }, "t1").start();

        Thread.sleep(100);

        new Thread(() -> {
            reentrantReadWriteLockTest.read();
        }, "t2").start();
    }

    private static void readAndWriteTest(ReentrantReadWriteLockTest reentrantReadWriteLockTest) throws InterruptedException {
        new Thread(() -> {
            reentrantReadWriteLockTest.write("你好🇨🇳");
        }, "t1").start();

        Thread.sleep(100);

        new Thread(() -> {
            reentrantReadWriteLockTest.read();
        }, "t2").start();
    }

    private Object read() {
        log.debug("获取读锁中...");
        readLock.lock();
        try {
            log.debug("开始读取数据：{}", data);
            JucUtils.sleepSecond(1);
        } finally {
            log.debug("读取数据完毕，释放读锁");
            readLock.unlock();
        }
        return data;
    }

    private void write(Object data) {
        log.debug("获取写锁中...");
        writeLock.lock();
        try {
            log.debug("开始写入数据");
            this.data = data;
            JucUtils.sleepSecond(1);
        } finally {
            log.debug("写入数据完毕，释放写锁");
            writeLock.unlock();
        }
    }
}
