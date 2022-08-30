package com.huazai.juc.test.utils;

import java.util.concurrent.TimeUnit;

/**
 * @author pyh
 * @datetime 2022/3/22 23:04
 * @description
 * @className JucUtils
 */
public class JucUtils {
    public static void sleepSecond(int seconds) {
        try {
            TimeUnit.SECONDS.sleep(seconds);
        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
            e.printStackTrace();
        }
    }

    public static void sleep(int time, TimeUnit timeUnit) {
        try {
            timeUnit.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
