package com.huazai.juc.test.aqs;

import com.huazai.juc.test.utils.JucUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Semaphore;

/**
 * 信号量，用来限制能同时访问共享资源的线程上限。
 *
 * @author pyh
 * @datetime 2022/4/10 12:18
 * @description
 * @className SemaphoreTest
 */
@Slf4j
public class SemaphoreTest {
    public static void main(String[] args) {
        Semaphore semaphore = new Semaphore(3);
        for (int i = 0; i < 20; i++) {
            int j = i;
            new Thread(() -> {
                try {
                    semaphore.acquire();
                    log.debug("begin{}", j);
                    JucUtils.sleepSecond(1);
                    log.debug("end{}", j);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    semaphore.release();
                }

            }).start();
        }
    }
}
