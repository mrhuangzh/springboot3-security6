package com.demo.security.handler.exception;

import com.alibaba.fastjson2.JSON;
import com.demo.security.enums.ResponseCodeEnum;
import com.demo.security.response.CommonResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * 未认证
 *
 * @Author: mrhuangzh
 * @Date: 2024/6/26 15:25
 **/
@Slf4j
public class CustomAuthenticationExceptionHandler implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        log.error("未认证", authException);
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        PrintWriter writer = response.getWriter();
        writer.print(JSON.toJSONString(CommonResponse.failed(ResponseCodeEnum.UNAUTHORIZED)));
        writer.flush();
        writer.close();
    }
}
