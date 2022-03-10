package com.huazai.juc.test.atomic;

import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;

/**
 * @author pyh
 * @datetime 2022/3/10 20:58
 * @description
 * @className AtomicReferenceFieldUpdaterTest
 */
public class AtomicReferenceFieldUpdaterTest {

    private volatile String name;

    public static void main(String[] args) {

        AtomicReferenceFieldUpdater<AtomicReferenceFieldUpdaterTest, String> nameFieldUpdater = AtomicReferenceFieldUpdater.newUpdater(AtomicReferenceFieldUpdaterTest.class, String.class, "name");
        AtomicReferenceFieldUpdaterTest atomicReferenceFieldUpdaterTest = new AtomicReferenceFieldUpdaterTest();
        nameFieldUpdater.compareAndSet(atomicReferenceFieldUpdaterTest, null, "hello world");

        System.out.println(atomicReferenceFieldUpdaterTest);
    }

    @Override
    public String toString() {
        return "AtomicReferenceFieldUpdaterTest{" +
                "name='" + name + '\'' +
                '}';
    }
}
