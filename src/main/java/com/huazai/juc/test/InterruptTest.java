package com.huazai.juc.test;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * 优雅的让子线程睡眠期间被打断后优雅的结束任务
 */
@Slf4j
public class InterruptTest {
    public static void main(String[] args) throws InterruptedException {
        Thread thread = new Thread(() -> {
            while (true) {
                log.debug("子线程判断自己是否被打断，{}", Thread.currentThread().isInterrupted());
                boolean interrupted = Thread.currentThread().isInterrupted();
                if (interrupted) {
                    log.debug("子线程被打断了，开始退出循环");
                    break;
                }
                try {
                    log.debug("子线程判断自己是否被打断，{}", Thread.currentThread().isInterrupted());
                    log.debug("子线程开始睡眠");
                    TimeUnit.SECONDS.sleep(10);
                    log.debug("子线程结束睡眠");
                } catch (InterruptedException e) {
                    log.debug("子线程睡眠被打断后自动重置打断状态，打断状态为：{}", Thread.currentThread().isInterrupted());
                    // 子线程睡眠时抛出被打断异常，重置打断标志，为了让代码能子线程能优雅的结束，子线程自行打断自己，下一层循环判断被打断了则退出循环
                    log.debug("子线程自行打断自己");
                    Thread.currentThread().interrupt();
                    log.debug("子线程自行打断自己后打断标志为：{}", Thread.currentThread().isInterrupted());
//                    e.printStackTrace();
                }
            }
        });
        thread.start();
        TimeUnit.SECONDS.sleep(2);
        log.debug("主线程开始打断子线程");
        thread.interrupt();
        TimeUnit.SECONDS.sleep(5);
        log.debug("主线程查看子线程打断状态：{}", thread.isInterrupted());
    }
}
