package com.huazai.juc.test.deadlock;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.ReentrantLock;

/**
 * 哲学家就餐问题（死锁）
 * <p>
 * 解决方案：
 * 1、改变加锁顺序，线程A和线程B分别都先获取a对象的monitor对象再获取b对象锁，缺点造成线程饥饿问题
 * 2、使用ReentrantLock
 */
@Slf4j
public class DiningPhilosopherProblemTest1 {
    public static void main(String[] args) {
        DiningPhilosopherProblemTest1 diningPhilosopherProblemTest = new DiningPhilosopherProblemTest1();
        Chopstick c1 = diningPhilosopherProblemTest.new Chopstick("1");
        Chopstick c2 = diningPhilosopherProblemTest.new Chopstick("2");
        Chopstick c3 = diningPhilosopherProblemTest.new Chopstick("3");
        Chopstick c4 = diningPhilosopherProblemTest.new Chopstick("4");
        Chopstick c5 = diningPhilosopherProblemTest.new Chopstick("5");
        diningPhilosopherProblemTest.new Philosopher("苏格拉底", c1, c2).start();
        diningPhilosopherProblemTest.new Philosopher("柏拉图", c2, c3).start();
        diningPhilosopherProblemTest.new Philosopher("亚里士多德", c3, c4).start();
        diningPhilosopherProblemTest.new Philosopher("赫拉克利特", c4, c5).start();
        diningPhilosopherProblemTest.new Philosopher("阿基米德", c5, c1).start();
    }

    class Philosopher extends Thread {
        Chopstick left;
        Chopstick right;

        public Philosopher(String name, Chopstick left, Chopstick right) {
            super(name);
            this.left = left;
            this.right = right;
        }

        private void eat() {
            log.debug("eating...");
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            while (true) {
                if (left.tryLock()) {
                    try {
                        if (right.tryLock()) {
                            try {
                                eat();
                            } finally {
                                right.unlock();
                            }
                        }
                    } finally {
                        left.unlock();
                    }
                }
            }
        }
    }

    /**
     * 筷子类
     */
    class Chopstick extends ReentrantLock {
        String name;

        public Chopstick(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return "筷子{" + name + '}';
        }
    }
}


