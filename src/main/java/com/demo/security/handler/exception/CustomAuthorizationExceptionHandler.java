package com.demo.security.handler.exception;

import com.alibaba.fastjson2.JSON;
import com.demo.security.enums.ResponseCodeEnum;
import com.demo.security.response.CommonResponse;
import io.swagger.v3.core.util.Json;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * SpringSecurity框架捕捉到 AccessDeniedException 时<br/>
 * 或 认证成功但无权访问时
 *
 * @Author: mrhuangzh
 * @Date: 2024/6/26 15:29
 **/
@Slf4j
public class CustomAuthorizationExceptionHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException {
        log.error("无权访问", accessDeniedException);
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        response.setStatus(HttpStatus.FORBIDDEN.value());
        PrintWriter writer = response.getWriter();
        writer.print(JSON.toJSONString(CommonResponse.failed(ResponseCodeEnum.FORBIDDEN)));
        writer.flush();
        writer.close();
    }
}
