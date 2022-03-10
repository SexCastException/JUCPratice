package com.huazai.juc.test.atomic;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;

/**
 * @author pyh
 * @datetime 2022/3/10 20:37
 * @description
 * @className AtomicIntegerFieldUpdaterTest
 */
@Slf4j
public class AtomicIntegerFieldUpdaterTest {

    private volatile int number;

    public static void main(String[] args) {
        AtomicIntegerFieldUpdater<AtomicIntegerFieldUpdaterTest> fieldUpdater = AtomicIntegerFieldUpdater.newUpdater(
                AtomicIntegerFieldUpdaterTest.class, "number");

        AtomicIntegerFieldUpdaterTest atomicIntegerFieldUpdaterTest = new AtomicIntegerFieldUpdaterTest();
        fieldUpdater.compareAndSet(atomicIntegerFieldUpdaterTest, 0, 1);
        System.out.println(atomicIntegerFieldUpdaterTest);

    }

    @Override
    public String toString() {
        return "AtomicIntegerFieldUpdaterTest{" +
                "number=" + number +
                '}';
    }
}
