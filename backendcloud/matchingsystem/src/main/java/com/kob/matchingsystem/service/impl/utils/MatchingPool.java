package com.kob.matchingsystem.service.impl.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

@Component
public class MatchingPool extends Thread{
    private static List<Player> players = new ArrayList<>();
    private ReentrantLock lock = new ReentrantLock();
    private static RestTemplate restTemplate;
    final private static String startGameUrl = "http://127.0.0.1:3000/pk/start/game/"; // 该url在StartGameController.java中定义

    @Autowired
    public void setRestTemplate(RestTemplate restTemplate) {
        MatchingPool.restTemplate = restTemplate;
    }

    public void addPlayer(Integer userId, Integer ranking, Integer botId){
        lock.lock();
        try {
            players.add(new Player(userId, ranking, botId,0));
        } finally {
            lock.unlock();
        }
    }

    public void removePlayer(Integer userId){
        lock.lock();
        try {
            List<Player> newPlayers = new ArrayList<>();
            for (Player player : players) {
                if (!player.getUserId().equals(userId)) {
                    newPlayers.add(player);
                }
            }
            players = newPlayers;
        } finally {
            lock.unlock();
        }
    }

    // 当前匹配池中所有玩家的等待时间+1s
    private void increaseWaitingTime(){
        for (Player player : players) {
            player.setWaitingTime(player.getWaitingTime() + 1);
        }
    }

    //判断两名玩家是否可以匹配到一起
    private boolean checkMatched(Player a, Player b){
        int rankingDelta = Math.abs(a.getRanking() - b.getRanking());
        int waitingTime = Math.min(a.getWaitingTime(), b.getWaitingTime());
        return rankingDelta <= waitingTime * 10;
    }

    //向后端的StartGameController返回a和b的匹配结果
    private void sendResult(Player a, Player b){
//        System.out.println("send result: " + a + " " + b);
        MultiValueMap<String, String> data = new LinkedMultiValueMap<>();
        data.add("aId", a.getUserId().toString());
        data.add("a_botId", a.getBotId().toString());
        data.add("bId", b.getUserId().toString());
        data.add("b_botId", b.getBotId().toString());
        restTemplate.postForObject(startGameUrl, data, String.class);
    }

    //尝试匹配所有玩家
    private void matchPlayers(){
//        System.out.println("match players: " + players.toString());
        boolean[] used = new boolean[players.size()]; //用于存储这名玩家是有已经有人匹配了

        for (int i = 0; i < players.size(); i++) {
            if (used[i]) continue;
            for (int j = i + 1; j < players.size(); j++) {
                if (used[j]) continue;
                Player a = players.get(i);
                Player b = players.get(j);
                if (checkMatched(a, b)){
                    used[i] = true;
                    used[j] = true;
                    sendResult(a, b);
                    break;
                }
            }
        }

        //将已经匹配完的玩家移出匹配池
        List<Player> newPlayers = new ArrayList<>();
        for (int i = 0; i < players.size(); i++) {
            if (!used[i]) {
                newPlayers.add(players.get(i));
            };
        }
        players = newPlayers;
    }

    @Override
    public void run() {
        while(true){
            try {
                Thread.sleep(1000);
                lock.lock();
                try{
                    increaseWaitingTime();
                    matchPlayers();
                } finally {
                    lock.unlock();
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
