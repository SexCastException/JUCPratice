package com.huazai.juc.test.synchronous;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GuardedObject {
    private Object result;

    private Integer id;

    public GuardedObject() {
    }

    public GuardedObject(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    private final Object lock = new Object();

    public Object getResult(final long millis) throws InterruptedException {
        if (millis < 0) {
            throw new IllegalArgumentException("timeout value is negative");
        }

        // 开始的时间戳
        long startMillis = System.currentTimeMillis();
        // 已等待时间戳
        long waitedTimeMillis = 0l;
        synchronized (lock) {
            while (result == null) {
                if (millis == 0) {
                    log.debug("xxxxxxxxxxxxxx");
                    lock.wait(0);
                } else {
                    // 剩余等待的时间戳 = 需要等待的时间戳 - 已等待的时间戳
                    long restedTimeMillis = millis - waitedTimeMillis;
                    log.debug("剩余等待时间：{}", restedTimeMillis);
                    if (restedTimeMillis <= 0) {    // 必须考虑等于0的情况，否则会存在wait(0)的情况，会一直等待下去
                        // 剩余等待时间为负数，表示已经等待超时，此时不应该再等待
                        log.error("获取结果失败，等待获取结果超时！");
                        break;
                    }
                    lock.wait(restedTimeMillis);
                }
                waitedTimeMillis = System.currentTimeMillis() - startMillis;
            }
            return result;
        }
    }

    public Object getResult() throws InterruptedException {
        return getResult(0);
    }

    public void complete(Object result) {
        synchronized (lock) {
            this.result = result;
            lock.notifyAll();
        }
    }
}
