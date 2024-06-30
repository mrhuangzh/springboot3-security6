package com.demo.security.handler;

import com.alibaba.fastjson2.JSON;
import com.demo.security.enums.ResponseCodeEnum;
import com.demo.security.response.CommonResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * @Author: mrhuangzh
 * @Date: 2024/6/27 23:36
 **/
@Component
public class LoginFailHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        String errorMessage = exception.getMessage();
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        PrintWriter writer = response.getWriter();
        writer.print(JSON.toJSONString(CommonResponse.failed(ResponseCodeEnum.UNAUTHORIZED.getCode(), errorMessage)));
        writer.flush();
        writer.close();
    }
}
