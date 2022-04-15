package com.huazai.juc.test;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

/**
 * @author pyh
 * @datetime 2022/4/12 17:52
 * @description
 * @className FuturnTaskTest
 */
@Slf4j
public class FutureTaskTest {
    public static void main(String[] args) throws Exception {
        FutureTask<String> futureTask = new FutureTask(() -> {
            return "hello world";
        });

        new Thread().start();

        String result = futureTask.get();

        log.debug("result:{}", result);
    }
}
