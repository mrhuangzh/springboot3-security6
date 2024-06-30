package com.demo.security.service.impl;

import com.demo.security.entity.UserInfo;
import com.demo.security.service.UserService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * @Author: mrhuangzh
 * @Date: 2024/6/26 17:10
 **/
@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Resource
    private PasswordEncoder passwordEncoder;


    @Override
    public UserInfo getUserFromDB(String username) {
        UserInfo userInfo = new UserInfo();
        userInfo.setName("admin");
        userInfo.setPassword(passwordEncoder.encode("admin"));
        return userInfo;
    }

    @Override
    public UserInfo getUserByPhone(String phoneNumber) {
        if ("12345678901".equals(phoneNumber)) {
            UserInfo testUser = new UserInfo();
            testUser.setPassword(passwordEncoder.encode("manager"));
            return testUser;
        }
        return null;
    }

    @Override
    public UserInfo getUserByOpenId(String openId, String thirdPlatform) {
        log.info("通过openId从数据库查询user");
        if (thirdPlatform.equals("gitee")) {
            UserInfo testUser = new UserInfo();
            testUser.setPassword(passwordEncoder.encode("manager"));
            return testUser;
        }
        return null;
    }

    @Override
    public void createUserWithOpenId(UserInfo userInfo, String openId, String platform) {
        log.info("在数据库创建一个user，并绑定openId");
    }
}
