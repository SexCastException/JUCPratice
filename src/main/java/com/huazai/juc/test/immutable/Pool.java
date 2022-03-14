package com.huazai.juc.test.immutable;

import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.util.concurrent.atomic.AtomicIntegerArray;

/**
 * @author pyh
 * @datetime 2022/3/13 13:43
 * @description
 * @className Pool
 */
@Slf4j
public class Pool {
    public static void main(String[] args) {
        Pool pool = new Pool(3);
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                Connection connection = pool.borrow();
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                pool.release(connection);
            }, "t" + i).start();
        }
    }

    private Connection[] connections;

    private final int poolSize;

    /**
     * 0表示连接空闲，1表示繁忙
     */
    private AtomicIntegerArray status;

    public Pool(int poolSize) {
        this.poolSize = poolSize;
        connections = new Connection[poolSize];
        status = new AtomicIntegerArray(poolSize);
        for (int i = 0; i < poolSize; i++) {
            connections[i] = new MyConnection();
        }
    }

    public Connection borrow() {
        while (true) {
            for (int i = 0; i < poolSize; i++) {
                if (status.compareAndSet(i, 0, 1)) {
                    log.debug(Thread.currentThread().getName() + "成功获取数据库连接");
                    return connections[i];
                }
            }

            // 没有空闲的连接数
            synchronized (this) {
                try {
                    log.debug(Thread.currentThread().getName() + "等待获取数据库连接");
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void release(Connection connection) {
        if (connection == null) {
            throw new NullPointerException("释放的连接为空");
        }
        for (int i = 0; i < poolSize; i++) {
            if (connections[i] == connection) {
                status.set(i, 0);
                synchronized (this) {
                    log.debug(Thread.currentThread().getName() + "释放了数据库连接！");
                    // 释放了连接之后，唤醒其他正在等待连接资源的线程
                    notifyAll();
                }
            }
            break;
        }
    }
}
