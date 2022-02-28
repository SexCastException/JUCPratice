package com.huazai.juc.test.volatiles;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 双重检测单例模式
 *
 * @author pyh
 * @datetime 2022/2/28 21:09
 * @description
 * @className SingletonTest
 */
@Slf4j
final public class SingletonTest {
    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(9);
        for (int i = 0; i < 100; i++) {
            executorService.submit(() -> {
                SingletonTest instance = SingletonTest.getInstance();
                log.debug(instance + "");
            });
        }
        executorService.shutdown();
    }

    private SingletonTest() {

    }

    private volatile static SingletonTest singletonTest;

    public static SingletonTest getInstance() {
        /*
        synchronized外再加一层检查，是防止当单例对象创建好之后每次都获取monitor锁，提高性能
         */
        if (singletonTest == null) {
            synchronized (SingletonTest.class) {
                if (singletonTest == null) {
                /*
                    创建对象时，先从jvm申请一块内存地址，初始化完对象之后，将内存的引用地址赋值给变量
                    正常情况下：先调用构造方法初始化，然后再赋值引用
                    指令重排序：先赋值引用，再调用构造方法初始化，此时变量引用的是一个未完全初始化的一个半成品对象
                    volatile的作用：将修改后的变量立即写回主存，保证可见性；禁止指令重排序
                 */
                    singletonTest = new SingletonTest();
                }
            }
        }
        return singletonTest;
    }
}
