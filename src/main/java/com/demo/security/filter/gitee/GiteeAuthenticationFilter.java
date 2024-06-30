package com.demo.security.filter.gitee;

import com.alibaba.fastjson2.JSON;
import com.demo.security.entity.param.LoginParam;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.io.IOException;
import java.util.stream.Collectors;

/**
 * @Author: mrhuangzh
 * @Date: 2024/6/28 9:52
 **/
@Slf4j
public class GiteeAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    public GiteeAuthenticationFilter(AntPathRequestMatcher pathRequestMatcher,
                                     AuthenticationManager authenticationManager,
                                     AuthenticationSuccessHandler authenticationSuccessHandler,
                                     AuthenticationFailureHandler authenticationFailureHandler) {
        super(pathRequestMatcher);
        setAuthenticationManager(authenticationManager);
        setAuthenticationSuccessHandler(authenticationSuccessHandler);
        setAuthenticationFailureHandler(authenticationFailureHandler);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {

        String requestJsonData = request.getReader().lines()
                .collect(Collectors.joining(System.lineSeparator()));
        LoginParam loginParam = JSON.parseObject(requestJsonData, LoginParam.class);

        GiteeAuthentication authentication = new GiteeAuthentication(null);
        authentication.setGiteeCode(loginParam.getGiteeCode());
        authentication.setAuthenticated(false); // 提取参数阶段，authenticated一定是false
        return this.getAuthenticationManager().authenticate(authentication);
    }
}
