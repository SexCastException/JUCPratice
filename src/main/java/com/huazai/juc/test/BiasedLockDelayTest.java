package com.huazai.juc.test;

import lombok.extern.slf4j.Slf4j;
import org.openjdk.jol.info.ClassLayout;

import java.util.concurrent.TimeUnit;

/**
 * 1、如果开启了偏向锁（默认开启），那么对象创建后，markword 值为 0x05 即最后 3 位为 101，这时它的thread、epoch、age 都为 0。
 *
 * 2、偏向锁是默认是延迟的，不会在程序启动时立即生效，如果想避免延迟，可以加 VM 参数：-XX:BiasedLockingStartupDelay=0 来禁用延迟。
 *
 * 3、如果没有开启偏向锁，那么对象创建后，markword 值为 0x01 即最后 3 位为 001，这时它的 hashcode、age 都为 0，
 *    第一次用到 hashcode 时才会赋值，hashcode赋值后会禁用偏向锁，因为Mark Word记录了31位的hashcode，没有多余的空间记录54位的thread
 *
 * 4、-XX:-UseBiasedLocking：禁用偏向锁
 *
 * 5、偏向锁匙对轻量级锁的一种优化，自旋锁是对重量级锁的一种优化
 */
@Slf4j
public class BiasedLockDelayTest {
    public static void main(String[] args) throws InterruptedException {
        Dog dog = new Dog();
//        dog.hashCode();  // 生成hashcode禁用偏向锁

        new Thread(() -> {
            log.debug("synchronized 前");
            System.out.println(ClassLayout.parseInstance(dog).toPrintable());
            synchronized (dog) {
                log.debug("synchronized 中");
                System.out.println(ClassLayout.parseInstance(dog).toPrintable());
            }
            log.debug("synchronized 后");
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(ClassLayout.parseInstance(dog).toPrintable());
        }, "t1").start();
    }

}

class Dog{

}
