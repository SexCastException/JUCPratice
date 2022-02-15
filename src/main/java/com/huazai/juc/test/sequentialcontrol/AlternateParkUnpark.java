package com.huazai.juc.test.sequentialcontrol;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.LockSupport;

@Slf4j
public class AlternateParkUnpark {
    static Thread t1;
    static Thread t2;
    static Thread t3;

    public static void main(String[] args) {

        SyncParkUnpark syncParkUnpark = new SyncParkUnpark(50);

        t1 = new Thread(() -> {
            syncParkUnpark.printContent("a", t2);
        });

        t2 = new Thread(() -> {
            syncParkUnpark.printContent("b", t3);
        });

        t3 = new Thread(() -> {
            syncParkUnpark.printContent("c", t1);
        });
        t1.start();
        t2.start();
        t3.start();

        LockSupport.unpark(t1);
    }

    static class SyncParkUnpark {
        /**
         * 循环次数
         */
        Integer loopTimes;

        public SyncParkUnpark(Integer loopTimes) {
            this.loopTimes = loopTimes;
        }

        public void printContent(String printContent, Thread nextThread) {
            for (int i = 0; i < loopTimes; i++) {
                LockSupport.park();
                System.out.print(printContent);
                LockSupport.unpark(nextThread);
            }
        }
    }
}
