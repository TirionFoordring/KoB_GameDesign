package com.kob.backend.controller.pk;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@RequestMapping("/pk/")
public class BotInfoController {

    @RequestMapping("getbotinfo/")
    public List<Map<String,String>> getBotInfo(){

        List<Map<String,String>> list = new LinkedList<>();

        Map<String,String> bot1 = new HashMap<>();
        bot1.put("Name", "Sword");
        bot1.put("Version", "1.0");

        Map<String,String> bot2 = new HashMap<>();
        bot2.put("Name", "Storm");
        bot2.put("Version", "1.1");

        list.add(bot1);
        list.add(bot2);

        return list;
    }

}