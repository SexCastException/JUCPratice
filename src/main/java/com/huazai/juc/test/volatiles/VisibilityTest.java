package com.huazai.juc.test.volatiles;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * JMM 即 Java Memory Model，它定义了主存、工作内存抽象概念，底层对应着 CPU 寄存器、缓存、硬件内存、CPU 指令优化等。
 *
 * JMM 体现在以下几个方面：
 * 原子性 - 保证指令不会受到线程上下文切换的影响
 * 可见性 - 保证指令不会受 cpu 缓存的影响
 * 有序性 - 保证指令不会受 cpu 指令并行优化的影响
 */
@Slf4j
public class VisibilityTest {
    static /*volatile*/ boolean flag = true;
    public static void main(String[] args) throws InterruptedException {
        new Thread(() -> {
           while (flag) {
//               System.out.println(flag);
           }
        }).start();

        TimeUnit.SECONDS.sleep(1);

        flag = false;
    }

}
