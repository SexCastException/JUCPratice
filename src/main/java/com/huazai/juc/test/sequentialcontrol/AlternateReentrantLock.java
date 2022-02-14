package com.huazai.juc.test.sequentialcontrol;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 交替输出"abcabcabcabcabc"之waitNotify版，使用三个Condition对象，输出内容a时唤醒aCondition，
 * 输出内容b唤醒bCondition，输出内容c唤醒cCondition
 */
@Slf4j
public class AlternateReentrantLock {
    public static void main(String[] args) throws InterruptedException {
        ReentrantLock reentrantLock = new ReentrantLock();
        Condition aCondition = reentrantLock.newCondition();
        Condition bCondition = reentrantLock.newCondition();
        Condition cCondition = reentrantLock.newCondition();
        SyncReentrantLock syncReentrantLock = new SyncReentrantLock(50);

        new Thread(() -> {
            reentrantLock.lock();
            try {
                syncReentrantLock.printf("a", aCondition, bCondition);
            } finally {
                reentrantLock.unlock();
            }
        }).start();

        new Thread(() -> {
            reentrantLock.lock();
            try {
                syncReentrantLock.printf("b", bCondition, cCondition);
            } finally {
                reentrantLock.unlock();
            }
        }).start();

        new Thread(() -> {
            reentrantLock.lock();
            try {
                syncReentrantLock.printf("c", cCondition, aCondition);
            } finally {
                reentrantLock.unlock();
            }
        }).start();

        reentrantLock.lock();
        try {
            aCondition.signal();
        } finally {
            reentrantLock.unlock();
        }
    }

    static class SyncReentrantLock {
        // 循环次数
        Integer loopTimes;

        public SyncReentrantLock(Integer loopTimes) {
            this.loopTimes = loopTimes;
        }

        public void printf(String printContent, Condition currentCondition, Condition nextCondition) {
            for (int i = 0; i < loopTimes; i++) {
                try {
                    currentCondition.await();
                    System.out.print(printContent);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                nextCondition.signal();
            }

        }
    }
}
