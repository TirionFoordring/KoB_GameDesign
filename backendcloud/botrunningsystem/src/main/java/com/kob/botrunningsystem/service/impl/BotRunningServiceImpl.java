package com.kob.botrunningsystem.service.impl;

import com.kob.botrunningsystem.service.BotRunningService;
import com.kob.botrunningsystem.service.impl.utils.BotPool;
import org.springframework.stereotype.Service;

@Service
public class BotRunningServiceImpl implements BotRunningService {

    public final static BotPool botPool = new BotPool();

    @Override
    public String addBot(Integer userId, String botCode, String input) {
        botPool.addBot(userId, botCode, input);
//        System.out.println("Add bot: " + userId + " " + botCode + " " + input);
        return "Add bot successfully";
    }
}
