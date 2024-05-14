package com.kob.backend.controller.pk;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller //这种写法是前后端不分离的写法，即后端服务器给前段直接返回html，而不是返回部分数据到前段进行拼接（渲染）
@RequestMapping("/pk/")
public class IndexController {

    @RequestMapping("index/")
    public String index(){
        return "pk/index.html";
    }

}
