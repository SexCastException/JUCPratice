package com.huazai.juc.test.atomic;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicMarkableReference;
import java.util.concurrent.atomic.AtomicStampedReference;

/**
 * @author pyh
 * @datetime 2022/3/6 11:00
 * @description
 * @className AtomicStampedRefrerenceTest
 */
@Slf4j
public class AtomicStampedRefrerenceTest {
    static AtomicStampedReference<String> ref = new AtomicStampedReference<>("A", 0);

    public static void main(String[] args) throws InterruptedException {
        log.debug("main start...");
        // 获取值 A
        // 这个共享变量被它线程修改过？
        String prev = ref.getReference();
        int stamp = ref.getStamp();
        other();
        TimeUnit.SECONDS.sleep(1);
        // 尝试改为 C
        log.debug("change A->C {}", ref.compareAndSet(prev, "C", stamp, stamp + 1));

    }

    private static void other() throws InterruptedException {
        Thread t1 = new Thread(() -> {
            int stamp = ref.getStamp();
            log.debug("change A->B {}", ref.compareAndSet(ref.getReference(), "B", stamp, stamp + 1));
        }, "t1");

        t1.start();

        Thread t2 = new Thread(() -> {
            try {
                // 线程结束前对变量的写，对其它线程得知它结束后的读可见（比如其它线程调用 t1.isAlive() 或 t1.join()等待它结束）
                t1.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
//            t1.isInterrupted();
            int stamp = ref.getStamp();
            log.debug("change B->A {}", ref.compareAndSet(ref.getReference(), "A", stamp, stamp + 1));
        }, "t2");
        t2.start();

//        t1.join();
//        t2.join();
    }
}
