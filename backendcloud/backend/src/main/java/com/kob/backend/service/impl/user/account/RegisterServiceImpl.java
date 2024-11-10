package com.kob.backend.service.impl.user.account;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kob.backend.mapper.UserMapper;
import com.kob.backend.pojo.User;
import com.kob.backend.service.user.account.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RegisterServiceImpl implements RegisterService {

    @Autowired
    private UserMapper userMapper;

    //调用给密码加密的方法: passwordEncoder.encode()
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Map<String, String> register(String username, String password, String confirmedPassword) {
        Map<String, String> map = new HashMap<>();
        if (username == null) {
            map.put("error_message", "The username cannot be empty!");
            return map;
        }
        if (password == null || confirmedPassword == null) {
            map.put("error_message", "The password cannot be empty!");
            return map;
        }
        username = username.trim();
        if (username.isEmpty()) {
            map.put("error_message", "The username cannot be empty!");
            return map;
        }
        if (password.isEmpty() || confirmedPassword.isEmpty()) {
            map.put("error_message", "The password cannot be empty!");
            return map;
        }

        if (username.length() > 100) {
            map.put("error_message", "The username cannot exceed 100 characters!");
            return map;
        }
        if (password.length() > 100 || confirmedPassword.length() > 100) {
            map.put("error_message", "The password cannot exceed 100 characters!");
            return map;
        }
        if (!password.equals(confirmedPassword)) {
            map.put("error_message", "The passwords entered twice times must match!");
            return map;
        }

        //查询数据库中是否有username重复的用户
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        List<User> users = userMapper.selectList(queryWrapper);
        if (!users.isEmpty()) {
            map.put("error_message", "The username already exists!");
            return map;
        }

        String encryptPassword = passwordEncoder.encode(password);
        String photo = "https://cdn.acwing.com/media/article/image/2022/08/09/1_1db2488f17-anonymous.png";
        User user = new User(null, username, encryptPassword, photo, 1500);
        userMapper.insert(user); //将user信息注入数据库

        map.put("error_message", "Create account successfully!");
        return map;
    }
}
