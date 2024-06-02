package com.kob.backend.service.impl.user.bot;

import com.kob.backend.mapper.BotMapper;
import com.kob.backend.pojo.Bot;
import com.kob.backend.pojo.User;
import com.kob.backend.service.impl.utils.UserDetailsImpl;
import com.kob.backend.service.user.bot.UpdateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class UpdateServiceImpl implements UpdateService {

    @Autowired
    private BotMapper botMapper;

    @Override
    public Map<String, String> update(Map<String, String> data) {

        UsernamePasswordAuthenticationToken authenticationToken = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl loginUser = (UserDetailsImpl) authenticationToken.getPrincipal();
        User user = loginUser.getUser();

        int bot_id = Integer.parseInt(data.get("bot_id"));
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

        Bot bot = botMapper.selectById(bot_id);

        if (bot == null){
            map.put("error_message", "ERROR! The bot does not exist or has been deleted.");
            return map;
        }

        if (!bot.getUserId().equals(user.getId())){
            map.put("error_message", "ERROR! You do not have permission to delete this bot.");
            return map;
        }

        Bot newBot = new Bot(
                bot.getId(),
                user.getId(),
                title,
                description,
                content,
                bot.getRating(),
                bot.getCreatetime(),
                new Date()
        );

        //botMapper.insert()用于向数据库中插入新bot，botMapper.updateById()用于更新
        botMapper.updateById(newBot);

        map.put("error_message", "SUCCESS! Bot has been updated.");
        return map;

    }
}
