package com.huazai.juc.test.aqs;

import com.huazai.juc.test.utils.JucUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * @author pyh
 * @datetime 2022/4/11 09:18
 * @description
 * @className CyclicBarrierTest
 */
@Slf4j
public class CyclicBarrierTest {
    public static void main(String[] args) {
        CyclicBarrier cyclicBarrier = new CyclicBarrier(2);
//        for (int i = 0; i < 100; i++) {
        new Thread(() -> {
            try {
                log.debug("t1线程启动");
                cyclicBarrier.await();
                log.debug("t1运行结束");
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (BrokenBarrierException e) {
                e.printStackTrace();
            }

        }, "t1").start();

        new Thread(() -> {
            try {
                log.debug("t2线程启动");
                log.debug("t2线程开始睡眠2秒...");
                JucUtils.sleepSecond(2);
                log.debug("t2线程睡眠结束");
                cyclicBarrier.await();
                log.debug("t2运行结束");
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (BrokenBarrierException e) {
                e.printStackTrace();
            }
        }, "t2").start();
//        }
    }
}
