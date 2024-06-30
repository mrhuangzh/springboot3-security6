package com.demo.security.controller;

import com.demo.security.response.CommonResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

/**
 * @Author: mrhuangzh
 * @Date: 2024/6/28 11:04
 **/
@Slf4j
@RestController
@RequestMapping("/auth/login/gitee")
@Tag(name = "gitee 配置", description = "用于获取 gitee 三方登录")
public class LoginGiteeController {

    @Value("${login.gitee.clientId}")
    private String giteeClientId;

    @Value("${login.gitee.redirectUri}")
    private String giteeCallbackEndpoint;

    @GetMapping("/config")
    @Operation(summary = "gitee 配置", description = "用于获取 gitee 三方登录")
    public CommonResponse<Object> getConfig() {
        HashMap<String, Object> config = new HashMap<>();
        config.put("clientId", giteeClientId);
        config.put("redirectUri", giteeCallbackEndpoint);
        return CommonResponse.success(config);

    }
}
