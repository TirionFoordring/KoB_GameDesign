package com.kob.backend.service.impl.user.bot;

import com.kob.backend.mapper.BotMapper;
import com.kob.backend.pojo.Bot;
import com.kob.backend.pojo.User;
import com.kob.backend.service.impl.utils.UserDetailsImpl;
import com.kob.backend.service.user.bot.AddService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class AddServiceImpl implements AddService {

    @Autowired
    private BotMapper botMapper;

    @Override
    public Map<String, String> add(Map<String, String> data) {

        //获取当前用户信息的固定写法
        UsernamePasswordAuthenticationToken authenticationToken = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl loginUser = (UserDetailsImpl) authenticationToken.getPrincipal();
        User user = loginUser.getUser();

        String title = data.get("title");
        String description = data.get("description");
        String content = data.get("content");
        Map<String, String> map = new HashMap<>();

        if (title == null || title.isEmpty()) {
            map.put("error_message", "The title cannot be empty");
            return map;
        }

        if (title.length() > 100) {
            map.put("error_message", "The title cannot be longer than 100 characters");
            return map;
        }

        if (description == null || description.isEmpty()) {
            map.put("error_message", "The description cannot be empty");
            return map;
        }

        if (description.length() > 300) {
            map.put("error_message", "The description cannot be longer than 300 characters");
            return map;
        }

        if (content == null || content.isEmpty()) {
            map.put("error_message", "The content cannot be empty");
            return map;
        }

        if (content.length() > 10000) {
            map.put("error_message", "The content cannot be longer than 10000 characters");
            return map;
        }

        Date now = new Date(); //获取当前时间
        Bot bot = new Bot(null, user.getId(), title, description, content, 1500, now, now);
        botMapper.insert(bot); //将bot信息注入数据库
        map.put("error_message", "Add bot successfully.");

        return map;
    }
}
