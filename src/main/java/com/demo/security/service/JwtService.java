package com.demo.security.service;

import org.springframework.beans.factory.InitializingBean;

/**
 * @Author: mrhuangzh
 * @Date: 2024/6/26 19:31
 **/
public interface JwtService extends InitializingBean {
    String createJwt(Object jwtPayload, long expiredAt);
    <T> T verifyJwt(String jwt, Class<T> jwtPayloadClass);
}
