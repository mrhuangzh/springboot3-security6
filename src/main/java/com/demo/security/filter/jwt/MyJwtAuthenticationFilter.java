package com.demo.security.filter.jwt;

import com.demo.security.entity.UserLoginInfo;
import com.demo.security.enums.ResponseCodeEnum;
import com.demo.security.exception.BusinessException;
import com.demo.security.service.JwtService;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.annotation.Resource;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * @Author: mrhuangzh
 * @Date: 2024/6/26 19:31
 **/
@Slf4j
@AllArgsConstructor
public class MyJwtAuthenticationFilter extends OncePerRequestFilter {


    @Resource
    private JwtService jwtService;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String jwtToken = request.getHeader("Authorization");
        if (StringUtils.isEmpty(jwtToken)) {
            throw new BusinessException(ResponseCodeEnum.UNAUTHORIZED.getCode(),"jwt 不存在");
        }
        if (jwtToken.startsWith("Bearer ")) {
            jwtToken = jwtToken.substring(7);
        }
        try {
            UserLoginInfo userLoginInfo = jwtService.verifyJwt(jwtToken, UserLoginInfo.class);

            MyJwtAuthentication authentication = new MyJwtAuthentication(null);
            authentication.setJwtToken(jwtToken);
            authentication.setAuthenticated(true); // 设置true，认证通过。
            authentication.setCurrentUser(userLoginInfo);
            // 认证通过后，一定要设置到SecurityContextHolder里面去。
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }catch (ExpiredJwtException e) {
            // 转换异常，指定code，让前端知道时token过期，去调刷新token接口
            throw new BusinessException(ResponseCodeEnum.UNAUTHORIZED.getCode(),"jwt 过期");
        } catch (Exception e) {
            throw new BusinessException(ResponseCodeEnum.UNAUTHORIZED.getCode(),"jwt 无效");
        }
        // 放行
        filterChain.doFilter(request, response);

    }
}
