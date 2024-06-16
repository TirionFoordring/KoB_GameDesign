package com.kob.matchingsystem.service.impl.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Player {
    private Integer userId;
    private Integer ranking;
    private Integer waitingTime; // 匹配时的等待时间
}
