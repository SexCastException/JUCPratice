package com.huazai.juc.test.pool.jdkpool;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

/**
 * @author pyh
 * @datetime 2022/3/27 10:46
 * @description
 * @className ForkJoinTaskTest1
 */
@Slf4j
public class ForkJoinTaskTest1 extends RecursiveTask<Integer> {
    public static void main(String[] args) {
        ForkJoinPool forkJoinPool = new ForkJoinPool(2);
        Integer result = forkJoinPool.invoke(new ForkJoinTaskTest1(1, 5));
        log.debug("result:{}", result);
    }

    private int begin;

    private int end;

    public ForkJoinTaskTest1(int begin, int end) {
        this.begin = begin;
        this.end = end;
    }

    @Override
    public String toString() {
        return "ForkJoinTaskTest1{" +
                "begin=" + begin +
                ", end=" + end +
                '}';
    }

    @Override
    protected Integer compute() {
        if (begin == end) {
            return end;
        }
        if (end - begin == 1) {
            return begin + end;
        }

        int mid = (end + begin) / 2;
        ForkJoinTaskTest1 fork1 = new ForkJoinTaskTest1(begin, mid);
        fork1.fork();
        ForkJoinTaskTest1 fork2 = new ForkJoinTaskTest1(mid + 1, end);
        fork2.fork();

        int result = fork1.join() + fork2.join();
        return result;
    }
}
