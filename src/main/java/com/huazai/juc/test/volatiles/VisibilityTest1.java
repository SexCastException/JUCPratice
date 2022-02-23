package com.huazai.juc.test.volatiles;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * Java内存模型中，synchronized规定，线程在加锁时，先清空工作内存，在主内存总拷贝最新变量的副本到工作内存，
 * 释放互斥锁之前将更改后的共享变量的值刷新到主内存中
 *
 *
 * 05-004
 */
@Slf4j
public class VisibilityTest1 {
    static boolean flag = true;
    static Object lock = new Object();
    public static void main(String[] args) throws InterruptedException {
        new Thread(() -> {
            while (true) {
                synchronized (lock) {
                    if (!flag) {
                        break;
                    }
                }
            }
        }).start();

        TimeUnit.SECONDS.sleep(1);


        flag = false;
    }
}
