package com.huazai.juc.test.synchronous;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ProtectivePause {
    public static void main(String[] args) throws InterruptedException {
        GuardedObject guardedObject = new GuardedObject();

        Thread t1 = new Thread(() -> {
            try {
                log.debug("t1线程开始获取结果。。。");
                Object result = guardedObject.getResult(1500);
                log.debug("t1线程成功获取到结果：{}", result);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "t1");
        t1.start();

        new Thread(() -> {
            log.debug("t2线程开始产生结果");
            guardedObject.complete(generateResult());
            log.debug("t2线程产生结果完毕！");
        }, "t2").start();
        t1.join();
        System.out.println(t1.isAlive());
    }

    public static Object generateResult() {
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "hello world";
    }
}
