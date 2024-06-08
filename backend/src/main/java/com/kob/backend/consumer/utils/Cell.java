package com.kob.backend.consumer.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cell { //这个类用来存储蛇的身体，目的是判断蛇当前所走的一步是否合法，没有撞到自己或对手的身体
    int x, y;
}
