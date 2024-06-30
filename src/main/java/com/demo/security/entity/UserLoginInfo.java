package com.demo.security.entity;

import lombok.Data;

/**
 * @Author: mrhuangzh
 * @Date: 2024/6/26 16:44
 **/
@Data
public class UserLoginInfo {
    private String name;
    private String password;
    private String sessionId; // 会话id，全局唯一
    private Long expiredTime; // 过期时间
    private String nickname;
}
