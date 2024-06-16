package com.kob.backend.controller.user.bot;

import com.kob.backend.pojo.Bot;
import com.kob.backend.service.user.bot.GetListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class GetListController {
    @Autowired
    private GetListService getListService;

    //涉及到修改数据库的操作用PostMapping，无需修改数据库的操作用GetMapping
    @GetMapping("/user/bot/getlist/")
    public List<Bot> getList(){
        return getListService.getList();
    }
}
