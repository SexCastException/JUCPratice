package com.huazai.juc.test.pool;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 线程安全阻塞消费队列
 *
 * @author pyh
 * @datetime 2022/3/17 21:52
 * @description
 * @className BlockingQueue
 */
@Slf4j
public class BlockingQueue<T> {
    public static void main(String[] args) {
        BlockingQueue<Integer> integerBlockingQueue = new BlockingQueue<>(4);

        for (int i = 0; i < 10000; i++) {
            int j = i;
            new Thread(() -> {
                boolean offer = integerBlockingQueue.offer(j, 1, TimeUnit.SECONDS);
            }, "生产队列" + i).start();

            new Thread(() -> {
                Integer poll = integerBlockingQueue.poll();
            }, "消费" + i).start();
        }
    }

    /**
     * 阻塞队列
     */
    private Queue<T> queue = new ArrayDeque<>();

    private ReentrantLock reentrantLock = new ReentrantLock();

    private Condition fullWaitCondition = reentrantLock.newCondition();

    private Condition emptyWaitCondition = reentrantLock.newCondition();

    /**
     * 队列容量大小
     */
    private final int capacity;

    private int count = 0;

    public BlockingQueue(int capacity) {
        this.capacity = capacity;
    }

    public BlockingQueue() {
        this(16);
    }

    public T poll() {
        reentrantLock.lock();
        try {
            while (queue.isEmpty()) {
                try {
                    log.debug(Thread.currentThread().getName() + "等待加入任务队列");
                    emptyWaitCondition.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            T poll = queue.poll();
            count--;
            log.debug("消费" + poll + "，当前队列大小：{}", count);
//            log.debug(Thread.currentThread().getName() + "唤醒其他线程消费任务队列");
            fullWaitCondition.signalAll();
            return poll;
        } finally {
            reentrantLock.unlock();
        }
    }

    public T poll(long timeout, TimeUnit timeUnit) {
        reentrantLock.lock();
        try {
            while (queue.isEmpty()) {
                try {
                    log.debug(Thread.currentThread().getName() + "等待加入任务队列");
                    emptyWaitCondition.await(timeout, timeUnit);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            T poll = queue.poll();
            count--;
            log.debug("消费" + poll + "，当前队列大小：{}", count);
//            log.debug(Thread.currentThread().getName() + "唤醒其他线程消费任务队列");
            fullWaitCondition.signalAll();
            return poll;
        } finally {
            reentrantLock.unlock();
        }
    }

    public boolean offer(T e) {
        reentrantLock.lock();
        try {
            while (queue.size() == capacity) {
                try {
                    log.debug(Thread.currentThread().getName() + "等待消费队列");
                    fullWaitCondition.await();
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
            boolean offer = queue.offer(e);
            count++;
            log.debug("加入" + e + "，当前队列大小：{}", count);
//            log.debug(Thread.currentThread().getName() + "唤醒其他线程加入任务队列");
            emptyWaitCondition.signalAll();
            return offer;
        } finally {
            reentrantLock.unlock();
        }
    }

    public boolean offer(T e, long timeout, TimeUnit timeUnit) {
        reentrantLock.lock();
        try {
            long toNanos = timeUnit.toNanos(timeout);
            while (queue.size() == capacity) {
                try {
                    log.debug(Thread.currentThread().getName() + "等待消费队列");
                    toNanos = fullWaitCondition.awaitNanos(toNanos);
//                    fullWaitCondition.await(timeout, timeUnit);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
            boolean offer = queue.offer(e);
            count++;
            log.debug("加入" + e + "，当前队列大小：{}", count);
            emptyWaitCondition.signalAll();
//            log.debug(Thread.currentThread().getName() + "唤醒其他线程加入任务队列");
            return offer;
        } finally {
            reentrantLock.unlock();
        }
    }
}
