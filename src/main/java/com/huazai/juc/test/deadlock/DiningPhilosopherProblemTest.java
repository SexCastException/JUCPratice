package com.huazai.juc.test.deadlock;

import lombok.extern.slf4j.Slf4j;

/**
 * 哲学家就餐问题（死锁）
 *
 * 解决方案：
 * 1、改变加锁顺序，打破死锁的条件，线程A和线程B分别都先获取a对象的monitor对象再获取b对象锁，缺点造成线程饥饿问题
 * 2、使用ReentrantLock
 */
@Slf4j
public class DiningPhilosopherProblemTest {
    public static void main(String[] args) {
        DiningPhilosopherProblemTest diningPhilosopherProblemTest = new DiningPhilosopherProblemTest();
        Chopstick c1 = diningPhilosopherProblemTest.new Chopstick("1");
        Chopstick c2 = diningPhilosopherProblemTest.new Chopstick("2");
        Chopstick c3 = diningPhilosopherProblemTest.new Chopstick("3");
        Chopstick c4 = diningPhilosopherProblemTest.new Chopstick("4");
        Chopstick c5 = diningPhilosopherProblemTest.new Chopstick("5");
        diningPhilosopherProblemTest.new Philosopher("苏格拉底", c1, c2).start();
        diningPhilosopherProblemTest.new Philosopher("柏拉图", c2, c3).start();
        diningPhilosopherProblemTest.new Philosopher("亚里士多德", c3, c4).start();
        diningPhilosopherProblemTest.new Philosopher("赫拉克利特", c4, c5).start();
//        diningPhilosopherProblemTest.new Philosopher("阿基米德", c5, c1).start();
        // 改变加锁顺序，打破获取锁的一个闭环条件
        diningPhilosopherProblemTest.new Philosopher("阿基米德", c1, c5).start();
    }

    class Philosopher extends Thread {
        final Chopstick left;
        final Chopstick right;

        public Philosopher(String name, Chopstick left, Chopstick right) {
            super(name);
            this.left = left;
            this.right = right;
        }

        private void eat() {
            log.debug(this.getName() + " eating...");
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            while (true) {
                // 获得左手筷子
                synchronized (left) {
                    // 获得右手筷子
                    synchronized (right) {
                        // 吃饭
                        eat();
                    }
                    // 放下右手筷子
                }
                // 放下左手筷子
            }
        }
    }

    /**
     * 筷子类
     */
    class Chopstick {
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


