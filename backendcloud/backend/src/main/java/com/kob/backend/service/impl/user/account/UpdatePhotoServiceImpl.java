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

    // 设置最大允许的图片大小 = 5MB
    private static final long MAX_IMAGE_SIZE = 5 * 1024 * 1024;

    @Override
    public Map<String, String> updatephoto(Map<String, String> data) {
        System.out.println("这段代码被运行了");

        UsernamePasswordAuthenticationToken authenticationToken = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl loginUser = (UserDetailsImpl) authenticationToken.getPrincipal();
        User user = loginUser.getUser();

        String profilePhoto = data.get("profilePhoto");

        Map<String, String> map = new HashMap<>();

        if (profilePhoto == null || profilePhoto.isEmpty()) {
            System.out.println("这个条件被触发了");
            map.put("error_message", "The profile photo cannot be empty");
            return map;
        }

        try {
            // 校验图片大小
            long imageSize = getBase64ImageSize(profilePhoto);
            if (imageSize > MAX_IMAGE_SIZE) {
                System.out.println("图片太大！");
                map.put("error_message", "The image file is too large. Maximum allowed size is 5MB.");
                return map;
            }

            System.out.println("图片大小符合要求，继续处理");

            // 更新用户头像
            userMapper.selectById(user.getId()).setPhoto(profilePhoto);
            map.put("error_message", "SUCCESS! Profile photo has been updated.");
            map.put("profilePhoto", profilePhoto);
            System.out.println("返回了success信息");

        } catch (Exception e) {
            // 捕获异常并返回错误信息
            System.out.println("处理过程中出现了异常: " + e.getMessage());
            map.put("error_message", "An error occurred while processing the image.");
        }

        return map;
    }

    // 估算图片大小
    private long getBase64ImageSize(String base64Image) {
        // Base64编码的字符串是以"data:image/..."开头的，可以去除掉前缀部分来计算实际的字节数
        String base64Data = base64Image.split(",")[1]; // 获取图片的Base64数据部分
        return base64Data.length() * 3 / 4; // Base64解码后的字节数大约是字符数的 3/4
    }
}
