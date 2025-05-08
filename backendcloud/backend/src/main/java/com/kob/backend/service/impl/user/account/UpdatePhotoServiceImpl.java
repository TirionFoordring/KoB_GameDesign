package com.kob.backend.service.impl.user.account;

import com.kob.backend.mapper.UserMapper;
import com.kob.backend.pojo.User;
import com.kob.backend.service.impl.utils.UserDetailsImpl;
import com.kob.backend.service.user.account.UpdatePhotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class UpdatePhotoServiceImpl implements UpdatePhotoService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public Map<String, String> updatephoto(Map<String, String> data) {
        UsernamePasswordAuthenticationToken authenticationToken =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl loginUser = (UserDetailsImpl) authenticationToken.getPrincipal();
        User user = loginUser.getUser();

        String idStr = data.get("selected_image_id");
        Map<String, String> map = new HashMap<>();

        if (idStr == null || !idStr.matches("[1-3]")) {
            map.put("error_message", "Invalid image id. Only 1, 2, or 3 allowed.");
            return map;
        }

        int id = Integer.parseInt(idStr);
        String photoUrl = getPhotoUrlById(id);

        user.setPhoto(photoUrl);
        userMapper.updateById(user);

        map.put("error_message", "SUCCESS! Profile photo has been updated.");
        map.put("profilePhoto", photoUrl);
        return map;
    }

    private String getPhotoUrlById(int id) {
        switch (id) {
            case 1:
                return "https://lh3.googleusercontent.com/24gpmSadEamr-vJRyQNEqjUWBuWmmplfmoNCzoipN8dfS-_9ul9jPn-Htz8vQONJpitB8TyVompK8VlGeN9lR6xDzyo=s137";
            case 2:
                return "https://lh3.googleusercontent.com/I_d143LyUop7Jch28lnBNGSv1FsSgtAaTkHH2-9O8hG9ZjfuHshjW8px5UXeW5FqUtW2zhPjq4-KAvvzfAJgN50JJg=s137";
            case 3:
                return "https://lh3.googleusercontent.com/XLnG6jiRDzz_jgeyzzb4TAE4KYGOfNkZeBa5-Ci-9tEhsI9B_flP86eXe4Ix70kATjsQasCJZhb1MWu5fDn3qAgMsw=s137";
            default:
                throw new IllegalArgumentException("Invalid image ID");
        }
    }
}

