package com.huazai.juc.test.atomic;

import sun.misc.Unsafe;

import java.lang.reflect.Field;

/**
 * 获取Unsafe对象
 *
 * @author pyh
 * @datetime 2022/3/10 21:05
 * @description
 * @className UnsafeAccessor
 */
public class UnsafeAccessor {
    private static Unsafe unsafe;

    static {
        try {
            Field theUnsafe = Unsafe.class.getDeclaredField("theUnsafe");
            theUnsafe.setAccessible(true);
            unsafe = (Unsafe) theUnsafe.get(null);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static Unsafe getUnsafe() {
        return unsafe;
    }

    public static void main(String[] args) {
        System.out.println(unsafe);
    }
}
