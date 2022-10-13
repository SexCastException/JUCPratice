package com.huazai.juc.test;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class ThreadLocalTest {
    private static ThreadLocal<Integer> threadLocal = new ThreadLocal<Integer>() {
        @Override
        protected Integer initialValue() {
            return 0;
        }
    };

    public static void main(String[] args) {
        Random random = new Random(100);
        threadLocal.set(random.nextInt());
        System.out.println("main线程本地线程变量：" + threadLocal.get());
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        try {
            for (int i = 0; i < 10; i++) {
                executorService.execute(() -> {
                    // 子线程获取不到主线程调用set方法的值，就默认返回initialValue方法的值，并为本线程创建专属本线程的副本，参考：ThreadLocal.setInitialValue
                    System.out.println(Thread.currentThread().getName() + "子线程本地线程变量：" + threadLocal.get());
                });
                threadLocal.set(random.nextInt());
                System.out.println("main线程本地线程变量：" + threadLocal.get());
            }
        } finally {
            executorService.shutdown();
        }
    }

}
