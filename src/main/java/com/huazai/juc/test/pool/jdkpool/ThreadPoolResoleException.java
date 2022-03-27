package com.huazai.juc.test.pool.jdkpool;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * @author pyh
 * @datetime 2022/3/26 11:04
 * @description
 * @className ThreadPoolResoleException
 */
@Slf4j
public class ThreadPoolResoleException {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(1);
        Future<Boolean> future = executorService.submit(() -> {
            log.debug("1");
            int i = 1 / 0;
            return true;
        });
        try {
            Boolean result = future.get();
            log.debug("result:{}", result);
        }catch (Exception e) {
            log.debug("线程池异步调用报错。");
            throw new RuntimeException(e);
        }

        executorService.shutdown();
    }
}
