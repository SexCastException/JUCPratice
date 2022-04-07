package com.huazai.juc.test.aqs;

import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * @author pyh
 * @datetime 2022/3/28 12:13
 * @description
 * @className MyLock
 */
@Slf4j
public class MyLock implements Lock, Serializable {

    protected MySync sync = new MySync();

    final class MySync extends AbstractQueuedSynchronizer {
        /**
         * 尝试加锁
         *
         * @param arg
         * @return
         */
        @Override
        protected boolean tryAcquire(int arg) {
            // 使用cas加锁
            if (compareAndSetState(0, 1)) {
                // 加锁成功后，设置owner为当前线程
                setExclusiveOwnerThread(Thread.currentThread());
                return true;
            }
            return false;
        }

        /**
         * 尝试释放锁
         *
         * @param arg
         * @return
         */
        @Override
        protected boolean tryRelease(int arg) {
            // 释放锁之后将owner线程设置为null
            setExclusiveOwnerThread(null);
            // 写操作之前加锁屏障
            setState(0);
            return true;
        }

        @Override
        protected boolean isHeldExclusively() {
            return getState() == 1;
        }

        public Condition newCondition() {
            return new ConditionObject();
        }
    }

    /**
     * 加锁，不成功进入等待队列
     */
    @Override
    public void lock() {
        sync.acquire(1);
    }

    /**
     * 加锁，不成功进入等待队列，可打断
     *
     * @throws InterruptedException
     */
    @Override
    public void lockInterruptibly() throws InterruptedException {
        sync.acquireInterruptibly(1);
    }

    /**
     * 加锁一次，不成功则返回false，不进入队列
     *
     * @return
     */
    @Override
    public boolean tryLock() {
        return sync.tryAcquire(1);
    }

    /**
     * 加锁一次，不成功进入等待队列，带等待超时时长
     *
     * @param time
     * @param unit
     * @return
     * @throws InterruptedException
     */
    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return sync.tryAcquireNanos(1, unit.toNanos(time));
    }

    @Override
    public void unlock() {
        sync.release(1);
    }

    @Override
    public Condition newCondition() {
        return sync.newCondition();
    }
}
