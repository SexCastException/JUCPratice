package com.huazai.juc.test.pool.jdkpool;

import com.huazai.juc.test.utils.JucUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * @author pyh
 * @datetime 2022/3/25 12:23
 * @description
 * @className HungryThreadPollTest
 */
@Slf4j
public class HungryThreadPollTest {
    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        executorService.execute(() -> {
            log.debug("开始点餐");
            Future<String> future = executorService.submit(() -> {
                JucUtils.sleepSecond(1);
                return "宫保鸡丁";
            });

            try {
                String s = future.get();
                log.debug("上菜：{}", s);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        });

        executorService.execute(() -> {
            log.debug("开始点餐");
            Future<String> future = executorService.submit(() -> {
                JucUtils.sleepSecond(1);
                return "辣子鸡";
            });
            try {
                String s = future.get();
                log.debug("上菜：{}", s);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

        });
//        executorService.shutdown();
    }
}
