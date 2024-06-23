package com.kob.backend.consumer.utils;

import com.alibaba.fastjson2.JSONObject;
import com.kob.backend.consumer.WebSocketServer;
import com.kob.backend.pojo.Bot;
import com.kob.backend.pojo.Record;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.locks.ReentrantLock;

// 该类用于在云端生成地图，将匹配的两个用户的游戏进行同步
public class Game extends Thread {
    final private Integer rows;
    final private Integer cols;
    final private Integer inner_walls_number;
    final private int[][] g;
    final private int[] dx = {-1, 0, 1, 0};
    final private int[] dy = {0, 1, 0, -1};
    final private Player playerA, playerB;
    private Integer nextStepA = null;
    private Integer nextStepB = null;
    private ReentrantLock lock = new ReentrantLock();
    private String gameStatus = "Playing"; // 表示整局游戏的状态，Playing表示正在进行，End表示游戏结束
    private String loser = ""; // 表示游戏的败方，all表示平局
    final private static String addBotUrl = "http://127.0.0.1:3002/bot/add/";

    public Game(Integer rows, Integer cols, Integer inner_walls_number, Integer idA, Bot botA, Integer idB, Bot botB) {
        this.rows = rows;
        this.cols = cols;
        this.inner_walls_number = inner_walls_number;
        this.g = new int[rows][cols];

        // 将两个Bot初始化并取出
        Integer botIdA = -1;
        Integer botIdB = -1;
        String botCodeA = "";
        String botCodeB = "";
        if (botA != null) {
            botIdA = botA.getId();
            botCodeA = botA.getContent();
        }
        if (botB != null) {
            botIdB = botB.getId();
            botCodeB = botB.getContent();
        }

        playerA = new Player(idA, botIdA, botCodeA, this.rows - 2, 1, new ArrayList<>());
        playerB = new Player(idB,botIdB, botCodeB, 1, this.cols - 2, new ArrayList<>());
    }

    public Player getPlayerA() { return playerA; }

    public Player getPlayerB() { return playerB; }

    public int[][] getG(){
        return this.g;
    }

    // 用锁将线程锁起来防止多个线程同时写一个变量时造成冲突
    public void setNextStepA(Integer nextStepA) {
        lock.lock();
        try{
            this.nextStepA = nextStepA;
        } finally {
            lock.unlock();
        }
    }

    public void setNextStepB(Integer nextStepB) {
        lock.lock();
        try{
            this.nextStepB = nextStepB;
        } finally {
            lock.unlock();
        }
    }

    private boolean drawMap(){
        // 初始化所有格子为空地（0）
        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < this.cols; j++) {
                this.g[i][j] = 0;
            }
        }

        // 给四周增加墙壁
        for (int r = 0; r < this.rows; r++) {
            this.g[r][0] = this.g[r][this.cols - 1] = 1;
        }
        for (int c = 0; c < this.cols; c++) {
            this.g[0][c] = this.g[this.rows - 1][c] = 1;
        }

        // 随机生成内部障碍物
        Random random = new Random();
        for (int i = 0; i < inner_walls_number/2; i++) {
            for (int j = 0; j < 1000; j++){
                int r = random.nextInt(this.rows);
                int c = random.nextInt(this.cols);

                // 如果此处已经是墙壁了，重新随机
                if (this.g[r][c] == 1 || this.g[this.rows - 1 - r][this.cols - 1 - c] == 1) continue;
                // 左下角和右上角禁止生成墙壁
                if ((r == this.rows - 2) && (c == 1) || (r == 1) && (c == this.cols - 2)) continue;

                this.g[r][c] = 1;
                this.g[this.rows - 1 - r][this.cols - 1 - c] = 1;
                break;
            }
        }

        return connectivity(this.rows - 2, 1, 1, this.cols - 2);
    }

    // 检验两名玩家间的连通性
    private boolean connectivity(int sx, int sy, int tx, int ty){
        if (sx == tx && sy == ty) return true;
        this.g[sx][sy] = 1;

        for (int i = 0; i < 4; i++) {
            int x = sx + this.dx[i], y = sy + this.dy[i];
            if (x >= 0 && x < this.rows && y >= 0 && y < this.cols && this.g[x][y] == 0){
                if (connectivity(x, y, tx, ty)){
                    this.g[sx][sy] = 0;
                    return true;
                }
            }
        }

        this.g[sx][sy] = 0;
        return false;
    }

    public void createMap(){ // 执行1000次画地图
        for (int i = 0; i < 1000; i++) {
            if (drawMap())
                break;
        }
    }

    // 将当前的局面信息编码成一个字符串
    // 编码格式为：地图信息 + "#" + 我的sx坐标 + "#" + 我的sy坐标+ "#(" + 我的操作序列（含历史记录） + ")#" +对手的sx坐标+ "#" + 对手的sy坐标 + "#(" + 对手的操作序列（含历史记录） + ")"
    private String getInput(Player player){
        Player myself, opponent;
        if (playerA.getId().equals(player.getId())) {
            myself = playerA;
            opponent = playerB;
        } else {
            myself = playerB;
            opponent = playerA;
        }

        return getMapString() + "#" + myself.getSx() + "#" + myself.getSy() + "#(" + myself.getStepsString() + ")#" + opponent.getSx() + "#" + opponent.getSy() + "#(" + opponent.getStepsString() + ")";
    }

    private void sendBotCode(Player player){
        // 判断是否是人工操作
        if (player.getBotId() == -1){
            return;
        }

        // 否则，按照BotRunningController中定义的类型传递数据
        MultiValueMap<String, String> data = new LinkedMultiValueMap<>();
        data.add("userId", player.getId().toString());
        data.add("botCode", player.getBotCode());
        data.add("input", getInput(player));

        // 使用WebSocket协议发送数据
        WebSocketServer.restTemplate.postForObject(addBotUrl, data, String.class);
    }

    // 等待两名玩家的下一步操作
    private boolean nextStep(){

        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        sendBotCode(playerA);
        sendBotCode(playerB);

        // 超过15秒钟无下一步操作判负
        for (int i = 0; i < 150; i++) {
            try {
                Thread.sleep(100);
                lock.lock();
                try {
                    if (nextStepA != null && nextStepB != null) {
                        playerA.getSteps().add(nextStepA);
                        playerB.getSteps().add(nextStepB);
                        return true;
                    }
                } finally {
                    lock.unlock();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
//        System.out.println("Failed to get both next steps in time.");
        return false;
    }

    private boolean checkValid(List<Cell> cellsA, List<Cell> cellsB){
        int n = cellsA.size();
        Cell tail = cellsA.get(n - 1);

        // 不能撞墙
        if (g[tail.x][tail.y] == 1) return false;

        // 不能碰到自己或对手的身体
        for (int i = 0; i < n - 1; i++) {
            if (cellsA.get(i).x == tail.x && cellsA.get(i).y == tail.y) return false;
        }
        for (int i = 0; i < n - 1; i++) {
            if (cellsB.get(i).x == tail.x && cellsB.get(i).y == tail.y) return false;
        }

        return true;
    }

    // 判断两名玩家的下一步操作是否合法
    private void judge(){
        // 先把两条蛇的身体都取出来
        List<Cell> cellsA = playerA.getCells();
        List<Cell> cellsB = playerB.getCells();

        boolean validA = checkValid(cellsA, cellsB);
        boolean validB = checkValid(cellsB, cellsA);
        if (!validA || !validB){
            this.gameStatus = "End";
            if (!validA && !validB){
                this.loser = "all";
            } else if (!validB) {
                this.loser = "B";
            } else {
                this.loser = "A";
            }
        }
    }

    private void sendAllMessage(String message){
        if (WebSocketServer.users.get(playerA.getId()) != null){
            WebSocketServer.users.get(playerA.getId()).sendMessage(message);
        }
        if (WebSocketServer.users.get(playerB.getId()) != null){
            WebSocketServer.users.get(playerB.getId()).sendMessage(message);
        }
    }

    // 向两名玩家分别返回对手的操作，同步游戏信息
    private void updateMove(){
        lock.lock();
        try{
            JSONObject resp = new JSONObject();
            resp.put("event", "move");
            resp.put("a_move", this.nextStepA);
            resp.put("b_move", this.nextStepB);
//            System.out.println("Sending move event: " + resp.toJSONString());
            sendAllMessage(resp.toJSONString());
            nextStepA = nextStepB = null;
        } finally {
            lock.unlock();
        }
    }

    // 将地图的数组转化为字符串，用于存储在数据库中
    private String getMapString(){
        StringBuilder resp = new StringBuilder();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                resp.append(this.g[i][j]);
            }
        }
        return resp.toString();
    }

    // 获取对局记录的辅助函数，用于存储在数据库中
    private void saveRecord(){
        Record record = new Record(
                null,
                playerA.getId(),
                playerA.getSx(),
                playerA.getSy(),
                playerB.getId(),
                playerB.getSx(),
                playerB.getSy(),
                playerA.getStepsString(),
                playerB.getStepsString(),
                getMapString(),
                this.loser,
                new Date()
        );
//        System.out.println("Record saved");
        WebSocketServer.recordMapper.insert(record);
    }

    // 向两名玩家返回游戏结局
    private void sendResult(){
        JSONObject resp = new JSONObject();
        resp.put("event", "result");
        resp.put("loser", this.loser);
        sendAllMessage(resp.toJSONString());
    }

    @Override
    public void run() {
        for (int i = 0; i < 1000; i++) {
//            System.out.println("this.status = " + this.gameStatus);
            // 先判断是否把两条蛇的下一步操作都获取到了
            if (nextStep()){
                judge();
                if ("Playing".equals(this.gameStatus)) {
//                    System.out.println("Calling updateMove()");
                    updateMove();
                } else {
                    saveRecord(); // 保存对局记录
                    sendResult(); // 向前端发送对局结果
                    break;
                }
            } else { // 没有操作则返回超时的一方判负
                this.gameStatus = "End";
                lock.lock();
                try {
                    if (nextStepA == null && nextStepB == null) {
                        this.loser = "all";
                    } else if (nextStepA == null) {
                        this.loser = "A";
                    } else {
                        this.loser = "B";
                    }
                } finally {
                    lock.unlock();
                }
                saveRecord(); // 保存对局记录
                sendResult(); // 向前端发送对局结果
                break;
            }
        }
    }
}
