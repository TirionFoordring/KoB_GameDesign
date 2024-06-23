package com.kob.matchingsystem.service.impl;

import com.kob.matchingsystem.service.MatchingService;
import com.kob.matchingsystem.service.impl.utils.MatchingPool;
import org.springframework.stereotype.Service;

@Service
public class MatchingServiceImpl implements MatchingService {
    public final static MatchingPool matchingPool = new MatchingPool();

    @Override
    public String addPlayer(Integer userId, Integer ranking, Integer botId) {
        System.out.println("Adding player: " + userId + ", ranking: " + ranking);
        matchingPool.addPlayer(userId, ranking, botId);
        return "Add Player Success";
    }

    @Override
    public String removePlayer(Integer userId) {
        System.out.println("Removing player: " + userId);
        matchingPool.removePlayer(userId);
        return "Remove Player Success";
    }
}
