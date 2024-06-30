package com.demo.security.handler;

import com.alibaba.fastjson2.JSON;
import com.demo.security.entity.UserLoginInfo;
import com.demo.security.enums.ResponseCodeEnum;
import com.demo.security.exception.BusinessException;
import com.demo.security.response.CommonResponse;
import com.demo.security.service.JwtService;
import com.demo.security.service.UserService;
import jakarta.annotation.Resource;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AbstractAuthenticationTargetUrlRequestHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @Author: mrhuangzh
 * @Date: 2024/6/27 23:17
 **/
@Component
public class LoginSuccessHandler extends
        AbstractAuthenticationTargetUrlRequestHandler implements AuthenticationSuccessHandler {


    @Resource
    private JwtService jwtService;




    public LoginSuccessHandler() {
        this.setRedirectStrategy(new RedirectStrategy() {
            @Override
            public void sendRedirect(HttpServletRequest request, HttpServletResponse response, String url)
                    throws IOException {
                // 更改重定向策略，前后端分离项目，后端使用RestFul风格，无需做重定向
                // Do nothing, no redirects in REST
            }
        });
    }




    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        Object principal = authentication.getPrincipal();
        if (principal == null || !(principal instanceof UserLoginInfo)) {
            throw new BusinessException(ResponseCodeEnum.UNAUTHORIZED.getCode(),
                    "登陆认证成功后，authentication.getPrincipal()返回的Object对象必须是：UserLoginInfo！");
        }
        UserLoginInfo currentUser = (UserLoginInfo) principal;
        currentUser.setSessionId(UUID.randomUUID().toString());

        // 生成token和refreshToken

        long expiredTime = System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(10); // 10分钟后过期
        currentUser.setExpiredTime(expiredTime);
        String token = jwtService.createJwt(currentUser, expiredTime);

        String refreshToken = jwtService.createJwt(currentUser, System.currentTimeMillis() + TimeUnit.DAYS.toMillis(30));

        Map<String, Object> responseData = new LinkedHashMap<>();
        responseData.put("token", token);
        responseData.put("refreshToken", refreshToken);

        // 一些特殊的登录参数。比如三方登录，需要额外返回一个字段是否需要跳转的绑定已有账号页面
        Object details = authentication.getDetails();
        if (details instanceof Map) {
            Map detailsMap = (Map)details;
            responseData.putAll(detailsMap);
        }

        // 虽然APPLICATION_JSON_UTF8_VALUE过时了，但也要用。因为Postman工具不声明utf-8编码就会出现乱码
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        PrintWriter writer = response.getWriter();
        writer.print(JSON.toJSONString( CommonResponse.success(responseData)));
        writer.flush();
        writer.close();
    }
}
