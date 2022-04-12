package com.huazai.juc.test.aqs;

import com.huazai.juc.test.utils.JucUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CountDownLatch;

/**
 * 用来进行线程同步协作，等待所有线程完成倒计时。
 *
 * @author pyh
 * @datetime 2022/4/10 12:25
 * @description
 * @className CountDownLatchTest
 */
@Slf4j
public class CountDownLatchTest {
    public static void main(String[] args) throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(3);
        new Thread(() -> {
            log.debug("begin...");
            JucUtils.sleepSecond(1);
            latch.countDown();
            log.debug("end...{}", latch.getCount());
        }).start();
        new Thread(() -> {
            log.debug("begin...");
            JucUtils.sleepSecond(2);
            latch.countDown();
            log.debug("end...{}", latch.getCount());
        }).start();
        new Thread(() -> {
            log.debug("begin...");
            JucUtils.sleepSecond(1);
            latch.countDown();
            log.debug("end...{}", latch.getCount());
        }).start();
        log.debug("waiting...");
        latch.await();
        log.debug("wait end...");
    }
}
