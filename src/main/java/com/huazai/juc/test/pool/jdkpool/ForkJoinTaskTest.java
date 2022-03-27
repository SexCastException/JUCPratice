package com.huazai.juc.test.pool.jdkpool;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

/**
 * @author pyh
 * @datetime 2022/3/27 10:17
 * @description
 * @className RecursiveTaskTest
 */
@Slf4j
public class ForkJoinTaskTest extends RecursiveTask<Integer> {
    public static void main(String[] args) {
        ForkJoinTaskTest forkJoinTaskTest = new ForkJoinTaskTest(5);
        // 错误调用
        /*Integer compute = forkJoinTaskTest.compute();
        System.out.println(compute);*/

        ForkJoinPool forkJoinPool = new ForkJoinPool(4);
        Integer result = forkJoinPool.invoke(forkJoinTaskTest);
        log.debug("result:{}", result);
    }

    private int n;

    public ForkJoinTaskTest(int n) {
        this.n = n;
    }

    @Override
    public String toString() {
        return "RecursiveTaskTest{" +
                "n=" + n +
                '}';
    }

    @Override
    protected Integer compute() {
        if (n == 1) {
            log.debug(Thread.currentThread().getName() + "递归出口，返回结果：{}", n);
            return n;
        }

        ForkJoinTaskTest forkJoinTaskTest = new ForkJoinTaskTest(n - 1);
        log.debug(Thread.currentThread().getName() + "开始分解任务，当前任务值：{}", n);
        forkJoinTaskTest.fork();

        int result = n + forkJoinTaskTest.join();
        log.debug(Thread.currentThread().getName() + "开始统计结果，统计之后的结果为：{}", result);
        return result;
    }
}
