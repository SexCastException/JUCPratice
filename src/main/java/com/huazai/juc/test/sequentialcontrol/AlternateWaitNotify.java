package com.huazai.juc.test.sequentialcontrol;

import lombok.extern.slf4j.Slf4j;

/**
 * 交替输出"abcabcabcabcabc"之waitNotify版，使用一个变量，标志是哪个线程输出什么内容
 * <p>
 * 输出内容     当前标志变量      下一个标志变量
 * a            1                2
 * b            2                3
 * c            3                1
 */
@Slf4j
public class AlternateWaitNotify {
    public static void main(String[] args) {
        SyncWaitNotify syncWaitNotify = new SyncWaitNotify(1, 50);

        new Thread(() -> {
            syncWaitNotify.printf("a", 1, 2);
        }).start();

        new Thread(() -> {
            syncWaitNotify.printf("b", 2, 3);
        }).start();

        new Thread(() -> {
            syncWaitNotify.printf("c", 3, 1);
        }).start();
    }

    static class SyncWaitNotify {
        // 标志变量
        Integer flag;
        // 循环次数
        Integer loopTimes;

        public SyncWaitNotify(Integer flag, Integer loopTimes) {
            this.flag = flag;
            this.loopTimes = loopTimes;
        }

        public void printf(String printContent, Integer currentFlag, Integer nextFlag) {
            synchronized (this) {
                for (int i = 0; i < loopTimes; i++) {
                    while (!currentFlag.equals(flag)) {
                        try {
                            wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    System.out.print(printContent);

                    flag = nextFlag;
                    notifyAll();
                }
            }
        }
    }
}
