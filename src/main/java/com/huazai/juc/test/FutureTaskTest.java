package com.huazai.juc.test;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * 当一个线程需要等待另一个线程把某个任务执行完后它才能继续执行，此时可以使用FutureTask。
 *
 * @author pyh
 * @datetime 2022/4/12 17:52
 * @description
 * @className FuturnTaskTest
 */
@Slf4j
public class FutureTaskTest {
    public static void main(String[] args) throws Exception {
//        futureTaskTest1();

        futureTaskTest2();
    }

    private static void futureTaskTest2() throws ExecutionException, InterruptedException {
        FutureTask<String> futureTaskA = new FutureTask<>(() -> {
            System.out.println(Thread.currentThread().getName() + "在call方法里");
            System.out.println(Thread.currentThread().getName() + "线程进入了 call方法，开始睡觉（进行了一些计算）");
            Thread.sleep(10000);
            System.out.println(Thread.currentThread().getName() + "睡醒了");
            return Thread.currentThread().getName() + "返回的：" + System.currentTimeMillis();
        });

        FutureTask<String> futureTaskB = new FutureTask<>(() -> {
            String s = futureTaskA.get();
            System.out.println("futureTaskB result" + s);
            System.out.println(Thread.currentThread().getName() + "在call方法里");
            return Thread.currentThread().getName() + "返回的：" + System.currentTimeMillis();
        });

        new Thread(futureTaskA, "线程A").start();
        new Thread(futureTaskB, "线程B").start();

        while (!futureTaskB.isDone()) {  //isDone表示FutureTask的计算是否完成
        }
        System.out.println(futureTaskA.get());
        System.out.println(futureTaskB.get());

        System.out.println(Thread.currentThread().getName() + "结束了");
    }


    private static void futureTaskTest1() throws InterruptedException, ExecutionException {
        FutureTask<String> futureTask = new FutureTask(() -> {
            log.debug("子线程执行");
            return "hello world";
        });

        new Thread(futureTask).start();

        String result = futureTask.get();

        log.debug("result:{}", result);
    }
}
