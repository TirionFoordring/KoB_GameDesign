package com.kob.matchingsystem.controller;

import com.kob.matchingsystem.service.MatchingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
public class MatchingController {
    @Autowired
    private MatchingService matchingService;

    @PostMapping("/player/add/")
    public String addPlayer(@RequestParam MultiValueMap<String, String> data) {
        //先将userId和ranking取出
        Integer userId = Integer.parseInt(Objects.requireNonNull(data.getFirst("userId")));
        Integer ranking = Integer.parseInt(Objects.requireNonNull(data.getFirst("ranking")));
        return matchingService.addPlayer(userId, ranking);
    }

    @PostMapping("/player/remove/")
    public String removePlayer(@RequestParam MultiValueMap<String, String> data) {
        Integer userId = Integer.parseInt(Objects.requireNonNull(data.getFirst("userId")));
        return matchingService.removePlayer(userId);
    }

}
