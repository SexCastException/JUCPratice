package com.huazai.juc.test.pool;

import lombok.extern.slf4j.Slf4j;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author pyh
 * @datetime 2022/3/17 21:50
 * @description
 * @className MyThreadPool
 */
@Slf4j
public class MyThreadPool {
    public static void main(String[] args) {
        MyThreadPool myThreadPool = new MyThreadPool(3, (queue, runnable) -> {
            System.out.println("使用拒绝策略处理无法执行的线程");
            new Thread(runnable).start();
        });
        for (int i = 0; i < 100; i++) {
            int j = i;
            myThreadPool.execute(() -> {
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                log.debug("执行任务" + j);
            });
        }
    }

    private BlockingQueue<Runnable> runnableBlockingQueue;

    private final int coreSize;

    private Set<Worker> workers = new HashSet<>();

    private RejectPolicy<Runnable> rejectPolicy;

    public MyThreadPool(int coreSize) {
        this(coreSize, null);
    }

    public MyThreadPool(int coreSize, RejectPolicy<Runnable> rejectPolicy) {
        this.coreSize = coreSize;
        this.runnableBlockingQueue = new BlockingQueue<>();
        this.rejectPolicy = rejectPolicy;
    }

    public void execute(Runnable runnable) {
        synchronized (workers) {
            // 任务数还没有达到核心线程数
            if (workers.size() < coreSize) {
                Worker worker = new Worker(runnable);
                workers.add(worker);
                log.debug("启动线程");
                worker.start();
            } else {
                log.debug("没有空闲线程，尝试将任务加入队列");
                boolean flag = runnableBlockingQueue.offer(runnable, 1, TimeUnit.SECONDS);
                if (flag) {
                    log.debug("加入任务队列成功");
                } else {
                    log.debug("加入任务队列失败");
                    if (rejectPolicy != null) {
                        log.debug("，并执行拒绝策略");
                        rejectPolicy.reject(runnableBlockingQueue, runnable);
                    }
                }

            }
        }
    }

    class Worker extends Thread {
        private Runnable runnable;

        public Worker(Runnable runnable) {
            this.runnable = runnable;
        }

        @Override
        public void run() {
            while (runnable != null || (runnable = runnableBlockingQueue.poll()) != null) {
                runnable.run();
                runnable = null;
            }

            synchronized (workers) {
                workers.remove(this);
            }
        }
    }


}

