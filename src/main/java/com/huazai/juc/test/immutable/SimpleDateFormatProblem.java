package com.huazai.juc.test.immutable;

import lombok.extern.slf4j.Slf4j;

import java.text.SimpleDateFormat;

/**
 * SimpleDateFormat属于可变属性并且非线程安全类
 *
 * @author pyh
 * @datetime 2022/3/13 12:51
 * @description
 * @className DateFormatterProblem
 */
@Slf4j
public class SimpleDateFormatProblem {
    public static void main(String[] args) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        for (int i = 0; i < 100; i++) {
            new Thread(() -> {
                try {
                    log.debug("{}", sdf.parse("1951-04-21"));
                } catch (Exception e) {
                    log.error("{}", e);
                }
            }).start();
        }
    }
}
