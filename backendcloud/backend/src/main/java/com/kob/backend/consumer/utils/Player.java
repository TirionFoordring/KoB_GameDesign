package com.kob.backend.consumer.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Player {
    private Integer id;
    private Integer botId; // botId = -1 则表示人工操作
    private String botCode;
    private Integer sx;
    private Integer sy;
    private List<Integer> steps;

    //检查蛇是否会变长
    private boolean checkTailIncrease(int step){
        if (step <= 10) return true;
        return step % 3 == 1;
    }

    public List<Cell> getCells(){
        List<Cell> res = new ArrayList<>();

        int[] dx = {-1, 0, 1, 0};
        int[] dy = {0, 1, 0, -1};

        int x = this.sx;
        int y = this.sy;
        int step = 0;

        res.add(new Cell(x,y));
        for (int d : steps){
            x += dx[d];
            y += dy[d];
            res.add(new Cell(x,y));
            step ++;
            if(!checkTailIncrease(++ step)){
                res.remove(0);
            }
        }
        return res;
    }

    // 获取操作序列
    public String getStepsString(){
        StringBuilder resp = new StringBuilder();
        for (Integer s : steps){
            resp.append(s);
        }
        return resp.toString();
    }
}
