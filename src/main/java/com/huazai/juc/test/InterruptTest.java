package com.huazai.juc.test;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * 先打断子线程，然后再睡眠，睡眠时抛出异常，打断标志为：false
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
                } catch (InterruptedException e) {
                    log.debug("子线程睡眠被打断");
                    log.debug("子线程睡眠被打断后判断打断标志状态，{}", Thread.currentThread().isInterrupted());
                    // 睡眠时抛出被打断异常，重置打断标志，让代码能子线程能优雅的结束
                    log.debug("重置子线程打断标志");
                    Thread.currentThread().interrupt();
                    log.debug("重置标志后判断子线程是否被打断，{}", Thread.currentThread().isInterrupted());
                    e.printStackTrace();
                }
            }
        });
       thread.start();
       TimeUnit.SECONDS.sleep(2);
       log.debug("开始打断子线程");
       thread.interrupt();
        TimeUnit.SECONDS.sleep(5);
       log.debug("子线程打断状态：{}", thread.isInterrupted());
    }
}
