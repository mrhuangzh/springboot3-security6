package com.demo.security.controller;

import com.alibaba.fastjson2.JSON;
import com.demo.security.entity.UserLoginInfo;
import com.demo.security.entity.param.LoginParam;
import com.demo.security.filter.username.UsernameAuthenticationProvider;
import com.demo.security.response.CommonResponse;
import com.demo.security.service.JwtService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @Author: mrhuangzh
 * @Date: 2024/6/26 16:39
 **/
@Slf4j
@RestController
@RequestMapping("/auth/login/username")
@Tag(name = "用户名密码登录相关", description = "用户名密码登录相关")
public class LoginUsernameController {

    @Resource
    private JwtService jwtService;


    @Resource
    UsernameAuthenticationProvider usernameAuthenticationProvider;

    @PostMapping("/login")
    @Operation(summary = "用户名密码登录", description = "用户名密码登录")
    public CommonResponse<Object> login(@RequestBody LoginParam param) {

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(param.getName(), param.getPassword());

        Authentication authenticate = usernameAuthenticationProvider.authenticate(authenticationToken);

        UserLoginInfo currentUser = (UserLoginInfo) authenticate.getPrincipal();
        currentUser.setSessionId(UUID.randomUUID().toString());

        // 生成token和refreshToken
        long expiredTime = System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(10); // 10分钟后过期
        currentUser.setExpiredTime(expiredTime);
        String token = jwtService.createJwt(currentUser, expiredTime);

        String refreshToken = jwtService.createJwt(currentUser, System.currentTimeMillis() + TimeUnit.DAYS.toMillis(30));

        Map<String, Object> responseData = new LinkedHashMap<>();
        responseData.put("token", token);
        responseData.put("refreshToken", refreshToken);

        return CommonResponse.success(JSON.toJSONString(responseData));
    }
}
