package com.demo.security.service;

import com.demo.security.entity.UserInfo;

/**
 * @Author: mrhuangzh
 * @Date: 2024/6/26 17:10
 **/
public interface UserService {
    UserInfo getUserFromDB(String username);
    UserInfo getUserByPhone(String phoneNumber);
    UserInfo getUserByOpenId(String openId, String thirdPlatform);
    void createUserWithOpenId(UserInfo userInfo, String openId, String platform);
}
