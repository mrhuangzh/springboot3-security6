package com.demo.security.handler.exception;

import com.alibaba.fastjson2.JSON;
import com.demo.security.enums.ResponseCodeEnum;
import com.demo.security.exception.BaseException;
import com.demo.security.exception.BusinessException;
import com.demo.security.response.CommonResponse;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * 捕捉 Spring security filter chain 中抛出的未知异常
 * @Author: mrhuangzh
 * @Date: 2024/6/26 15:52
 **/
@Slf4j
public class CustomSecurityExceptionHandler extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (BaseException e) {
            log.error("BaseException: ", e);
            extracted(response, CommonResponse.failed(e.getCode(), e.getMessage()));
        } catch (AuthenticationException e) {
            log.error("AuthenticationException: ", e);
            extracted(response, CommonResponse.failed(e.getMessage()));
        } catch (ExpiredJwtException e) {
            log.error("ExpiredJwtException: ", e);
            extracted(response, CommonResponse.failed(e.getMessage()));
        } catch (AccessDeniedException e) {
            log.error("AccessDeniedException: ", e);
            extracted(response, CommonResponse.failed(e.getMessage()));
        } catch (Exception e) {
            log.error("Exception: ", e);
            extracted(response, CommonResponse.failed(e.getMessage()));
        }
    }

    private static void extracted(HttpServletResponse response, CommonResponse<Object> e) throws IOException {
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        response.setStatus(HttpServletResponse.SC_OK);
        response.setStatus(HttpStatus.OK.value());
        PrintWriter writer = response.getWriter();
        writer.write(JSON.toJSONString(e));
        writer.flush();
        writer.close();
    }
}
