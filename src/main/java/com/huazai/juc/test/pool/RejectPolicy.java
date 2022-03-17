package com.huazai.juc.test.pool;

/**
 * 拒绝策略接口
 */
@FunctionalInterface
public interface RejectPolicy<T> {
    /**
     * 拒绝
     * @param queue
     * @param task
     */
    void reject(BlockingQueue queue, T task);
}
