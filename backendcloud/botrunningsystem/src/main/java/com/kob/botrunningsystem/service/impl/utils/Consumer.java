package com.kob.botrunningsystem.service.impl.utils;

import com.kob.botrunningsystem.utils.BotInterface;
import org.joor.Reflect;

import java.util.UUID;

public class Consumer extends Thread {
    private Bot bot;

    public void startTimeout(long timeout, Bot bot) {
        this.bot = bot;
        this.start();

        // startTimeout是主线程，主线程会被阻塞timeout毫秒的时间，如果在此时间内用户的程序始终没有返回操作，则视为超时
        // 这样做的目的是防止用户输入的程序将服务器锁死
        try {
            this.join(timeout); // 等待timeout毫秒
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            this.interrupt(); // 中断当前程序
        }

    }

    // 在code中的bot类名后面加随机uid
    private String addUid(String botCode, String uid) {
        int k = botCode.indexOf(" implements com.kob.botrunningsystem.utils.BotInterface");
        return botCode.substring(0, k) + uid + botCode.substring(k);
    }

    // 编译java代码，通过joor依赖实现
    @Override
    public void run() {

        // UUID用来每次返回一个不一样的随机字符串，将这个随机字符串加在类名后面防止重名的类仅被执行一次
        UUID uuid = UUID.randomUUID();
        String uid = uuid.toString().substring(0, 7);

        // 编译的第一项为类名，第二项为代码
        BotInterface botInterface = Reflect.compile(
                "com.kob.botrunningsystem.utils.Bot" + uid,
                addUid(bot.getBotCode(), uid)
        ).create().get();

        Integer direction = botInterface.nextMove(bot.getInput());

        System.out.println("The user " + bot.getUserId() + " has moved " + direction);
    }
}
