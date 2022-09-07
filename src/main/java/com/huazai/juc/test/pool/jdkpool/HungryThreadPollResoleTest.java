package com.huazai.juc.test.pool.jdkpool;

import com.huazai.juc.test.utils.JucUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * @author pyh
 * @datetime 2022/3/25 13:06
 * @description
 * @className HungryThreadPollResoleTest
 */
@Slf4j
public class HungryThreadPollResoleTest {
    public static void main(String[] args) {
        ExecutorService diancanExecutor = Executors.newFixedThreadPool(1);
        ExecutorService chaocaiExecutor = Executors.newFixedThreadPool(1);
        diancanExecutor.execute(() -> {
            log.debug("开始点餐");
            Future<String> future = chaocaiExecutor.submit(() -> {
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

        diancanExecutor.execute(() -> {
            log.debug("开始点餐");
            Future<String> future = chaocaiExecutor.submit(() -> {
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
        JucUtils.sleepSecond(8);
        log.debug(chaocaiExecutor.isShutdown() + "");
        log.debug(chaocaiExecutor.isTerminated() + "");

        diancanExecutor.shutdown();
        chaocaiExecutor.shutdown();
    }
}
