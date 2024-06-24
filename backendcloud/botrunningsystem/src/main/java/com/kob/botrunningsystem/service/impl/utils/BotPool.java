package com.kob.botrunningsystem.service.impl.utils;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class BotPool extends Thread {
    private final ReentrantLock lock = new ReentrantLock();
    private final Condition condition = lock.newCondition(); // 条件变量
    private final Queue<Bot> bots = new LinkedList<>();

    // 实现向队列bots中添加新的bot，由于bots可能被多个线程同时读写，所以要用锁保证读写不会冲突
    public void addBot(Integer userId, String botCode, String input){
        lock.lock();
        try {
            bots.add(new Bot(userId, botCode, input));
            condition.signal(); // 唤起线程
        } finally {
            lock.unlock();
        }
    }

    // 该函数为在后端执行用户输入代码的函数
    private void consume(Bot bot){
        Consumer consumer = new Consumer();
        consumer.startTimeout(2000, bot);
    }

    @Override
    public void run() {
        // 实现消息队列
        while (true) {
            lock.lock();
            if (bots.isEmpty()) {
                try {
                    condition.await(); // 阻塞当前线程直到该线程被唤醒为止，当其他线程中执行signal()或signalAll()时，可唤醒该线程
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    lock.unlock();
                    break;
                }
            } else {
                Bot bot = bots.remove();
                lock.unlock();
                consume(bot); //该函数相当耗时，可能会执行好几秒
            }
        }

    }
}
