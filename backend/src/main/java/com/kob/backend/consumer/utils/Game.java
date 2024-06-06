package com.kob.backend.consumer.utils;

import java.util.Random;

//该类用于在云端生成地图，将匹配的两个用户的游戏进行同步
public class Game {
    final private Integer rows;
    final private Integer cols;
    final private Integer inner_walls_number;
    final private int[][] g;
    final private int[] dx = {-1, 0, 1, 0};
    final private int[] dy = {0, 1, 0, -1};

    public Game(Integer rows, Integer cols, Integer inner_walls_number) {
        this.rows = rows;
        this.cols = cols;
        this.inner_walls_number = inner_walls_number;
        this.g = new int[rows][cols];
    }

    public int[][] getG(){
        return this.g;
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
}
