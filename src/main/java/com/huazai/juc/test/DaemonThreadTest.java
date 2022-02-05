package com.huazai.juc.test;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

@Slf4j
public class DaemonThreadTest {
    public static void main(String[] args) throws InterruptedException {
        log.debug("主线程开始运行...");
        Thread t1 = new Thread(() -> {
            while (true) {
                log.debug("子线程开始运行...");
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                log.debug("子线程运行结束...");
            }
        }, "daemon");
        // 设置该线程为守护线程
        t1.setDaemon(true);
        t1.start();
        TimeUnit.SECONDS.sleep(5);
        log.debug("主线程运行结束...");
    }
}
