package com.huazai.juc.test.threadlocal;


import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadLocalTest1 {

    private static Dog ddog = new Dog();
    private static ThreadLocal<Dog> threadLocal = new ThreadLocal<Dog>() {
        @Override
        protected Dog initialValue() {
            return ddog;
        }
    };

    public static void main(String[] args) {
        Random random = new Random(100);
        threadLocal.set(new Dog("叶狗"));
        System.out.println("main线程本地线程变量：" + threadLocal.get());
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        try {
            for (int i = 0; i < 10; i++) {
                executorService.execute(() -> {
                    // 子线程获取不到主线程调用set方法的值，就默认返回initialValue方法的值，并为本线程创建专属本线程的副本，参考：ThreadLocal.setInitialValue
                    System.out.println(Thread.currentThread().getName() + "子线程本地线程变量：" + threadLocal.get());
                });
                ddog.setName("刘狗" + i);
                threadLocal.set(ddog);
                System.out.println("main线程本地线程变量：" + threadLocal.get());
            }
        } finally {
            executorService.shutdown();
        }
    }

}
