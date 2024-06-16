package com.kob.backend.service.impl.user.account;

import com.kob.backend.pojo.User;
import com.kob.backend.service.impl.utils.UserDetailsImpl;
import com.kob.backend.service.user.account.LoginService;
import com.kob.backend.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    public Map<String, String> getToken(String username, String password){
        Map<String, String> map = new HashMap<>(); // 存放返回的结果

        // 将前端传来的username和password进行封装成 authenticationToken
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);

        // 将封装好的authenticationToken进行验证，判断是否合法，非法会自动报异常
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        // 若合法，则将其取出并赋予用户UserDetailsImpl类中的各种属性，形成loginUser
        UserDetailsImpl loginUser = (UserDetailsImpl) authenticate.getPrincipal();

        // 单独取loginUser中的用户信息，用于生成JWT-token
        User user = loginUser.getUser();
        String jwt = JwtUtil.createJWT(user.getId().toString());

        // 将生成的jwt-token传回给controller层
        map.put("error_message", "success");
        map.put("token", jwt);
        return map;

    }
}