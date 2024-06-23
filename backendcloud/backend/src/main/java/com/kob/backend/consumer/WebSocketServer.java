package com.kob.backend.consumer;


import com.alibaba.fastjson2.JSONObject;
import com.kob.backend.consumer.utils.Game;
import com.kob.backend.consumer.utils.JwtAuthentication;
import com.kob.backend.mapper.BotMapper;
import com.kob.backend.mapper.RecordMapper;
import com.kob.backend.mapper.UserMapper;
import com.kob.backend.pojo.Bot;
import com.kob.backend.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

//
@Component
@ServerEndpoint("/websocket/{token}")  // 注意不要以'/'结尾
public class WebSocketServer {

    //用线程安全的ConcurrentHashMap存储所有链接（每个链接是一个用户）
    public static ConcurrentHashMap<Integer, WebSocketServer> users = new ConcurrentHashMap<>();

    private User user;
    private Session session = null;

    private static UserMapper userMapper;
    public static RecordMapper recordMapper;
    public static RestTemplate restTemplate;
    private static BotMapper botMapper;
    private Game game = null;

    final private static String addPlayerUrl = "http://127.0.0.1:3001/player/add/";
    final private static String removePlayerUrl = "http://127.0.0.1:3001/player/remove/";

    @Autowired
    public void setUserMapper(UserMapper userMapper) {
        WebSocketServer.userMapper = userMapper;
    }

    @Autowired
    public void setRecordMapper(RecordMapper recordMapper) {
        WebSocketServer.recordMapper = recordMapper;
    }

    //restTemplate是在两个SpringBoot进程间进行通讯的工具
    @Autowired
    public void setRestTemplate(RestTemplate restTemplate) {
        WebSocketServer.restTemplate = restTemplate;
    }

    @Autowired
    public void setBotMapper(BotMapper botMapper) {
        WebSocketServer.botMapper = botMapper;
    }

    @OnOpen
    public void onOpen(Session session, @PathParam("token") String token) throws IOException {
        //建立链接
        this.session = session;
        System.out.println("Connected successfully.");
        Integer userId = JwtAuthentication.getUserId(token);
        this.user = userMapper.selectById(userId);

        if (user != null) {
            users.put(userId, this);
        } else {
            this.session.close();
        }

        System.out.println(users);
    }

    @OnClose
    public void onClose() {
        // 关闭链接(退出界面)
        System.out.println("Disconnected successfully.");
        if (this.user != null) {
            users.remove(this.user.getId());
        }
    }

    public static void startGame(Integer aId, Integer aBotId, Integer bId, Integer bBotId) {
        User a = userMapper.selectById(aId);
        User b = userMapper.selectById(bId);

        Bot botA = botMapper.selectById(aBotId);
        Bot botB = botMapper.selectById(bBotId);

        //将两名玩家的一局游戏生成一个新的Game类的实例
        Game game = new Game(13, 14, 20, a.getId(), botA, b.getId(), botB);
        game.createMap();
        //开启这个实例的线程（即，每两名玩家的一局游戏均是一个线程）
        game.start();

        if (users.get(a.getId()) != null) {
            users.get(a.getId()).game = game;
        }
        if (users.get(b.getId()) != null) {
            users.get(b.getId()).game = game;
        }


        //将地图相关信息封装成JSON
        JSONObject respGame = new JSONObject();
        respGame.put("a_id", game.getPlayerA().getId());
        respGame.put("a_sx", game.getPlayerA().getSx());
        respGame.put("a_sy", game.getPlayerA().getSy());
        respGame.put("b_id", game.getPlayerB().getId());
        respGame.put("b_sx", game.getPlayerB().getSx());
        respGame.put("b_sy", game.getPlayerB().getSy());
        respGame.put("map", game.getG());

        JSONObject respA = new JSONObject();
        respA.put("event", "start-game");
        respA.put("opponent_username", b.getUsername());
        respA.put("opponent_photo", b.getPhoto());
        respA.put("game", respGame);
        if (users.get(a.getId()) != null) {
            users.get(a.getId()).sendMessage(respA.toJSONString());
        }

        JSONObject respB = new JSONObject();
        respB.put("event", "start-game");
        respB.put("opponent_username", a.getUsername());
        respB.put("opponent_photo", a.getPhoto());
        respB.put("game", respGame);
        if (users.get(b.getId()) != null) {
            users.get(b.getId()).sendMessage(respB.toJSONString());
        }

    }

    //开始匹配
    private void startMatching(Integer botId){
        System.out.println("startMatching");
        MultiValueMap<String, String> data = new LinkedMultiValueMap<>();

        //此处添加的的字段在MatchingController.java中定义
        data.add("userId", this.user.getId().toString());
        data.add("ranking", this.user.getRanking().toString());
        data.add("bot_id", botId.toString());

        restTemplate.postForObject(addPlayerUrl, data, String.class);
    }

    //取消匹配
    private void cancelMatching(){
        System.out.println("cancelMatching");
        MultiValueMap<String, String> data = new LinkedMultiValueMap<>();

        data.add("userId", this.user.getId().toString());

        restTemplate.postForObject(removePlayerUrl, data, String.class);
    }

    //获取人的操作并传给Game
    private void move(int direction){
        if (game.getPlayerA().getId().equals(this.user.getId())){
            // 如果是人工操作再去获取人的操作，否则屏蔽掉人的操作
            if (game.getPlayerA().getBotId() == -1){
                game.setNextStepA(direction);
                System.out.println("Player A moved: " + direction);
            }
        } else if (game.getPlayerB().getId().equals(this.user.getId())){
            // 如果是人工操作再去获取人的操作，否则屏蔽掉人的操作
            if (game.getPlayerB().getBotId() == -1){
                game.setNextStepB(direction);
                System.out.println("Player B moved: " + direction);
            }
        }
    }

    @OnMessage
    public void onMessage(String message, Session session) { //此函数通常被当做路由使用（即做很多条件判断，判断接下来应该交给哪个函数处理）
        // 从Client接收消息
//        System.out.println("received message: " + message);
        JSONObject data = JSONObject.parseObject(message);
        String event = data.getString("event"); //对应前端MatchGround.vue中向后端传入消息的event
        if ("start-matching".equals(event)) {
            startMatching(data.getInteger("bot_id"));
        } else if ("cancel-matching".equals(event)) {
            cancelMatching();
        } else if ("move".equals(event)) {
            move(data.getInteger("direction"));
        }
    }

    @OnError
    public void onError(Session session, Throwable error) {
        error.printStackTrace();
    }

    //从后端向前端发送信息（手动实现）
    public void sendMessage(String message) {
        synchronized (this.session) {
            try {
                this.session.getBasicRemote().sendText(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
