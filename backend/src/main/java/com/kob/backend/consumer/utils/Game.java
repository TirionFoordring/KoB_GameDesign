package com.kob.backend.consumer.utils;

import com.alibaba.fastjson2.JSONObject;
import com.kob.backend.consumer.WebSocketServer;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.locks.ReentrantLock;

//该类用于在云端生成地图，将匹配的两个用户的游戏进行同步
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
    private String gameStatus = "Playing"; //表示整局游戏的状态，Playing表示正在进行，End表示游戏结束
    private String loser = ""; //表示游戏的败方，all表示平局

    public Game(Integer rows, Integer cols, Integer inner_walls_number, Integer idA, Integer idB) {
        this.rows = rows;
        this.cols = cols;
        this.inner_walls_number = inner_walls_number;
        this.g = new int[rows][cols];
        playerA = new Player(idA, this.rows - 2, 1, new ArrayList<>());
        playerB = new Player(idB, 1, this.cols - 2, new ArrayList<>());
    }

    public Player getPlayerA() { return playerA; }

    public Player getPlayerB() { return playerB; }

    public int[][] getG(){
        return this.g;
    }

    //用锁将线程锁起来防止多个线程同时写一个变量时造成冲突
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
        //初始化所有格子为空地（0）
        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < this.cols; j++) {
                this.g[i][j] = 0;
            }
        }

        //给四周增加墙壁
        for (int r = 0; r < this.rows; r++) {
            this.g[r][0] = this.g[r][this.cols - 1] = 1;
        }
        for (int c = 0; c < this.cols; c++) {
            this.g[0][c] = this.g[this.rows - 1][c] = 1;
        }

        //随机生成内部障碍物
        Random random = new Random();
        for (int i = 0; i < inner_walls_number/2; i++) {
            for (int j = 0; j < 1000; j++){
                int r = random.nextInt(this.rows);
                int c = random.nextInt(this.cols);

                //如果此处已经是墙壁了，重新随机
                if (this.g[r][c] == 1 || this.g[this.rows - 1 - r][this.cols - 1 - c] == 1) continue;
                //左下角和右上角禁止生成墙壁
                if ((r == this.rows - 2) && (c == 1) || (r == 1) && (c == this.cols - 2)) continue;

                this.g[r][c] = 1;
                this.g[this.rows - 1 - r][this.cols - 1 - c] = 1;
                break;
            }
        }

        return connectivity(this.rows - 2, 1, 1, this.cols - 2);
    }

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

    public void createMap(){ //执行1000次画地图
        for (int i = 0; i < 1000; i++) {
            if (drawMap())
                break;
        }
    }

    //等待两名玩家的下一步操作
    private boolean nextStep(){

        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        //超过5秒钟无下一步操作判负
        for (int i = 0; i < 50; i++) {
            try {
                Thread.sleep(1000);
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
        System.out.println("Failed to get both next steps in time.");
        return false;
    }

    //判断两名玩家的下一步操作是否合法
    private void judge(){

    }

    private void sendAllMessage(String message){
        WebSocketServer.users.get(playerA.getId()).sendMessage(message);
        WebSocketServer.users.get(playerB.getId()).sendMessage(message);
    }

    //向两名玩家分别返回对手的操作，同步游戏信息
    private void updateMove(){
        lock.lock();
        try{
            JSONObject resp = new JSONObject();
            resp.put("event", "move");
            resp.put("a_move", this.nextStepA);
            resp.put("b_move", this.nextStepB);
            System.out.println("Sending move event: " + resp.toJSONString());
            sendAllMessage(resp.toJSONString());
            nextStepA = nextStepB = null;
        } finally {
            lock.unlock();
        }
    }


    //向两名玩家返回游戏结局
    private void sendResult(){
        JSONObject resp = new JSONObject();
        resp.put("event", "result");
        resp.put("loser", this.loser);
        sendAllMessage(resp.toJSONString());
    }

    @Override
    public void run() {
        for (int i = 0; i < 1000; i++) {
            System.out.println("this.status = " + this.gameStatus);
            //先判断是否把两条蛇的下一步操作都获取到了
            if (nextStep()){
                judge();
                if ("Playing".equals(this.gameStatus)) {
                    System.out.println("Calling updateMove()");
                    updateMove();
                } else {
                    sendResult();
                    break;
                }
            } else {
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
                break;
            }
        }
    }
}
