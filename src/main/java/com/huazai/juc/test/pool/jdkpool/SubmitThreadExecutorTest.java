package com.huazai.juc.test.pool.jdkpool;

import com.huazai.juc.test.utils.JucUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;

/**
 * @author pyh
 * @datetime 2022/3/22 23:20
 * @description
 * @className ThreadExecutorTest
 */
@Slf4j
public class SubmitThreadExecutorTest {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        Future<String> future = executorService.submit(() -> {
            log.debug("start sleepping");
            JucUtils.sleepSecond(1);
            return "hello world";
        });
        log.debug("result:{}", future.get());

        Future<?> future1 = executorService.submit(() -> {
            log.debug("start sleepping");
            JucUtils.sleepSecond(1);
        });
        log.debug("result1:{}", future1.get());

        FutureTask<String> futureTask = new FutureTask<String>(() -> {
            log.debug("start sleepping");
            JucUtils.sleepSecond(1);
            return "中华人民共和国";
        });
        new Thread(futureTask).start();
        log.debug("futureTask result:{}", futureTask.get());

        invokeAllTest(executorService);

        invokeAnyTest(executorService);

        executorService.shutdown();
    }

    private static void invokeAnyTest(ExecutorService executorService) throws InterruptedException, ExecutionException {
    /*
        invokeAny提交所有，返回线程最快执行完毕的结果，并且打断其他线程的任务，如果其他线程正在睡眠，则抛出InterruptedException
     */
        String sResult = executorService.invokeAny(Arrays.asList(
                () -> {
                    log.debug("invokeAny sleep");
                    JucUtils.sleepSecond(1);
                    return "1";
                },
                () -> {
                    log.debug("invokeAny sleep");
                    JucUtils.sleepSecond(1);
                    return "2";
                },
                () -> {
                    log.debug("invokeAny sleep");
                    JucUtils.sleepSecond(1);
                    return "3";
                }
        ));
        log.debug("invokeAny result:{}", sResult);
    }

    private static void invokeAllTest(ExecutorService executorService) throws InterruptedException {
        // invokeAll提交所有线程，并且等到所有结果返回，有带超时时长的重载函数
        List<Future<String>> futures = executorService.invokeAll(Arrays.asList(
                () -> {
                    log.debug("invokeAll sleep");
                    JucUtils.sleepSecond(1);
                    return "1";
                },
                () -> {
                    log.debug("invokeAll sleep");
                    JucUtils.sleepSecond(1);
                    return "2";
                },
                () -> {
                    log.debug("invokeAll sleep");
                    JucUtils.sleepSecond(5);
                    return "3";
                }
        ));
        /*
         遍历invokeAll结果时，等待所有线程执行完之后才开始取结果，此时即使第一个线程已经执行完并且结果也已经返回，
         所以取结果等待时长取决于执行时长最长的那个线程 + 线程等待时长（线程不够就需 要等待）
         */
        futures.forEach(t -> {
            try {
                String result = t.get();
                log.debug("invokeAll result: {}", result);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

        });
    }


}
