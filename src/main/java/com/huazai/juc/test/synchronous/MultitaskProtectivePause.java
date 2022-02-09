package com.huazai.juc.test.synchronous;

import lombok.extern.slf4j.Slf4j;

import java.util.Hashtable;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Slf4j
public class MultitaskProtectivePause {
    public static void main(String[] args) throws InterruptedException {
        Mailboxes mailboxes = new Mailboxes();

        for (int i = 0; i < 3; i++) {
            new MultitaskProtectivePause.People(mailboxes).start();
        }
        TimeUnit.SECONDS.sleep(1);
        for (Integer id : mailboxes.getIds()) {
            new Postman(id, "内容" + id, mailboxes).start();
        }

    }

    static class People extends Thread{
        private Mailboxes mailboxes;

        public People(Mailboxes mailboxes) {
            this.mailboxes = mailboxes;
        }

        @Override
        public void run() {

            try {
                // 收信
                GuardedObject guardedObject = mailboxes.createGuardedObject();
                log.debug("开始收信 id:{}", guardedObject.getId());
                Object mail = null;
                mail = guardedObject.getResult(5000);
                log.debug("收到信 id:{}, 内容:{}", guardedObject.getId(), mail);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    static class Postman extends Thread {
        private int id;
        private String mail;
        private Mailboxes mailboxes;

        public Postman(int id, String mail, Mailboxes mailboxes) {
            this.id = id;
            this.mail = mail;
            this.mailboxes = mailboxes;
        }
        @Override
        public void run() {
            GuardedObject guardedObject = mailboxes.getGuardedObject(id);
            log.debug("送信 id:{}, 内容:{}", id, mail);
            guardedObject.complete(mail);
        }
    }

    static class Mailboxes {
        private Map<Integer, GuardedObject> boxes = new ConcurrentHashMap<>();
        private int id = 1;
        // 产生唯一 id
        private synchronized int generateId() {
            return id++;
        }
        public GuardedObject getGuardedObject(int id) {
            return boxes.remove(id);
        }
        public GuardedObject createGuardedObject() {
            GuardedObject go = new GuardedObject(generateId());
            boxes.put(go.getId(), go);
            return go;
        }
        public Set<Integer> getIds() {
            return boxes.keySet();
        }
    }

}




