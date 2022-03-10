package com.huazai.juc.test.atomic;

import lombok.extern.slf4j.Slf4j;
import sun.misc.Unsafe;

import java.io.Serializable;

/**
 * @author pyh
 * @datetime 2022/3/10 21:17
 * @description
 * @className MyAtomicIntegerTest
 */
@Slf4j
public class MyAtomicIntegerTest implements Serializable {
    private volatile int value;

    private static final long valueOffset;

    public MyAtomicIntegerTest(int value) {
        this.value = value;
    }

    public MyAtomicIntegerTest() {
    }

    private static final Unsafe unSafe = UnsafeAccessor.getUnsafe();

    static {
        try {
            valueOffset = unSafe.objectFieldOffset(MyAtomicIntegerTest.class.getDeclaredField("value"));
        } catch (NoSuchFieldException e) {
            throw new Error(e);
        }
    }

    public boolean compareAndSet(int expect, int updated) {
        return unSafe.compareAndSwapInt(this, valueOffset, expect, updated);
    }

    @Override
    public String toString() {
        return "MyAtomicIntegerTest{" +
                "value=" + value +
                '}';
    }

    public static void main(String[] args) {
        MyAtomicIntegerTest myAtomicIntegerTest = new MyAtomicIntegerTest(0);
        myAtomicIntegerTest.compareAndSet(0, 1);
        System.out.println(myAtomicIntegerTest);
    }
}
