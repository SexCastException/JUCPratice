package com.huazai.juc.test.volatiles;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * JMM 即 Java Memory Model，它定义了主存、工作内存抽象概念，底层对应着 CPU 寄存器、缓存、硬件内存、CPU 指令优化等。
 * <p>
 * JMM 体现在以下几个方面：
 * 原子性 - 保证指令不会受到线程上下文切换的影响
 * 可见性 - 保证指令不会受 cpu 缓存的影响
 * 有序性 - 保证指令不会受 cpu 指令并行优化的影响
 * <p>
 * happens-before：
 * 1、线程解锁 m 之前对变量的写，对于接下来对 m 加锁的其它线程对该变量的读可见。
 * 2、线程对 volatile 变量的写，对接下来其它线程对该变量的读可见。
 * 3、线程 start 前对变量的写，对该线程开始后对该变量的读可见。
 * 4、线程结束前对变量的写，对其它线程得知它结束后的读可见（比如其它线程调用 t1.isAlive() 或 t1.join()等待它结束）
 * 5、线程 t1 打断 t2（interrupt）前对变量的写，对于其他线程得知 t2 被打断后对变量的读可见（通过t2.interrupted 或 t2.isInterrupted）。这条和上一条有些类似。
 * 6、对变量默认值（0，false，null）的写，对其它线程对该变量的读可见
 * 7、具有传递性，如果 x hb-> y 并且 y hb-> z 那么有 x hb-> z ，配合 volatile 的防指令重排
 */
@Slf4j
public class VisibilityTest {
    static /*volatile*/ boolean flag = true;
    public static void main(String[] args) throws InterruptedException {
        new Thread(() -> {
            while (flag) {
               /*
                    主线程将循环条件flag值改动了，如果没有volatile修饰，子线程不可见，导致一直都没退出循环
                    如果执行有synchronize语句，由于synchronize底层特性，即使flag没有volatile修饰，也会让子线程对flag可见。
                    只要符合happens-before原则，不加volatile也能使变量可见
                */
//               System.out.println(flag);
           }
        }).start();

        TimeUnit.SECONDS.sleep(1);

        flag = false;
    }

}
