package com.huazai.juc.test.atomic;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicMarkableReference;

/**
 * 和AtomicStampedRefrerence类似，AtomicMarkableReference也能感知原子引用修改之前是否被其他线程修改过。
 * 但是AtomicStampedRefrerence还能感知被修改过几次。
 *
 * @author pyh
 * @datetime 2022/3/6 12:29
 * @description
 * @className AtomicMarkableReferenceTest
 */
@Slf4j
public class AtomicMarkableReferenceTest {
    static AtomicMarkableReference<String> ref = new AtomicMarkableReference<>("A", true);

    public static void main(String[] args) throws InterruptedException {
        log.debug("main start...");
        // 获取值 A
        // 这个共享变量被它线程修改过？
        String prev = ref.getReference();
        other();
        TimeUnit.SECONDS.sleep(1);
        // 尝试改为 C
        log.debug("change A->C {}", ref.compareAndSet(prev, "C", true, false));
    }

    public static void other() throws InterruptedException {
        new Thread(() -> {
            log.debug("change A->B {}", ref.compareAndSet(ref.getReference(), "B", true, false));
        }, "t1").start();

        new Thread(() -> {
            log.debug("change B->A {}", ref.compareAndSet(ref.getReference(), "A", false, true));
        }, "t2").start();
    }
}
