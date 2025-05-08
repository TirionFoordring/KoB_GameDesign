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

        if (idStr == null || !idStr.matches("[1-7]")) {
            map.put("error_message", "Invalid image id. Only 1-6 allowed.");
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
            case 4:
                return "https://lh3.googleusercontent.com/Qifv3yehXafCJkJ28bHB4UrmGy-U6Vy_EG9HYmPLv-ad-hrGHnFGx5djS_5PKkiig0vVcRQvu2M2SoAljLM7fXK5G3o=s137";
            case 5:
                return "https://lh3.googleusercontent.com/6XG4XRahS3ODxEMUDit1-Nit2C8vw48Xbbext7KNOr0SEkL-ng4yOQhYxOmg9PqjukQ3VSq5zJcLx7YWffmf_r28lw=s137";
            case 6:
                return "https://lh3.googleusercontent.com/IX8GJbAVsWdPXjV_FXPIpci81jsyqEML8iSzPoeksmlP5mDQhzr1FFdgsv_bsRGggUJYfdIK1Zx7SM7fW-z05BaLDg=s137";
            case 7:
                return "https://lh3.googleusercontent.com/5WOnn9PwMwlrBRZvSvzexoGC5gTGaKTP_NlTcNLRwKXgitb3Hb--W07inTNwM1HsY1bNpkTfcaqCK_93ausVyC8N=s137";
            default:
                throw new IllegalArgumentException("Invalid image ID");
        }
    }
}

