package com.huazai.juc.test.asynchronous;

import lombok.extern.slf4j.Slf4j;

import java.util.LinkedList;

@Slf4j
public class MessageQueue {
    public static void main(String[] args) {
        MessageQueue messageQueue = new MessageQueue(5);

        new Thread(() -> {
            for (int i = 0; i < 100; i++) {
                log.debug("生产者开始生产第{}条消息", i + 1);
                messageQueue.put(new Message(i, "hello world " + (i + 1)));
            }
        }, "生产者线程").start();

        new Thread(() -> {
            while(true) {
//                log.debug("消费者开始消费消息");
                Message message = messageQueue.take();
                log.debug("消费消息成功，消息内容：{}", message);
            }
        }, "消费者线程").start();
    }

    public MessageQueue(int capacity) {
        this.capacity = capacity;
    }

    /**
     * 消息队列容器
     */
    private LinkedList<Message> queue = new LinkedList<>();

    /**
     * 队列的最大容量
     */
    private int capacity;

    public Message take() {
        synchronized (queue) {
            if (queue.isEmpty()) {
                try {
                    log.debug("消息队列为空，正在等待生产者生产消息。。。");
                    queue.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            // 先进先出
            Message message = queue.removeFirst();
            queue.notifyAll();
            return message;
        }
    }

    public void put(Message message) {
        synchronized (queue) {
            if (queue.size() >= capacity) {
                try {
                    log.debug("消息队列已满，正在等待消费者消费消息。。。");
                    queue.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            queue.add(message);
            queue.notifyAll();
        }

    }
}
