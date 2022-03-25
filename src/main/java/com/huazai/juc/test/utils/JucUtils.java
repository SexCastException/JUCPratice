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
            e.printStackTrace();
        }
    }
}
