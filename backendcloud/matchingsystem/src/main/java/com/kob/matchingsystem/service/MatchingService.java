package com.kob.matchingsystem.service;

public interface MatchingService {
    String addPlayer(Integer userId, Integer ranking);
    String removePlayer(Integer userId);
}
