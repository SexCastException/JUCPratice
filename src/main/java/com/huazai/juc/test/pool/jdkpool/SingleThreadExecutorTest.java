package com.huazai.juc.test.pool.jdkpool;

import com.huazai.juc.test.utils.JucUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 希望多个任务排队执行。线程数固定为 1，任务数多于 1 时，会放入无界队列排队。任务执行完毕，这唯一的线程也不会被释放。
 * 区别：
 * 自己创建一个单线程串行执行任务，如果任务执行失败而终止那么没有任何补救措施，而线程池还会新建一个线程，保证池的正常工作
 * Executors.newSingleThreadExecutor() 线程个数始终为1，不能修改
 * FinalizableDelegatedExecutorService 应用的是装饰器模式，只对外暴露了 ExecutorService 接口，因此不能调用 ThreadPoolExecutor 中特有的方法
 * Executors.newFixedThreadPool(1) 初始时为1，以后还可以修改
 * 对外暴露的是 ThreadPoolExecutor 对象，可以强转后调用 setCorePoolSize 等方法进行修改
 *
 * @author pyh
 * @datetime 2022/3/22 22:51
 * @description
 * @className newSingleThreadExecutorTest
 */
@Slf4j
public class SingleThreadExecutorTest {
    public static void main(String[] args) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(() -> {
            int i = 10 / 0;
            log.debug("1");
            JucUtils.sleepSecond(1);
        });

        executorService.execute(() -> {
            // 及时之前的任务抛出了异常也不影响本次任务的执行
            log.debug("2");
        });
    }
}
