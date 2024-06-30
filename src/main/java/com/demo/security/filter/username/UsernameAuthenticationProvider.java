package com.demo.security.filter.username;

import com.demo.security.entity.UserInfo;
import com.demo.security.entity.UserLoginInfo;
import com.demo.security.exception.BusinessException;
import com.demo.security.service.UserService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import static com.demo.security.enums.ResponseCodeEnum.UNAUTHORIZED;

/**
 * @Author: mrhuangzh
 * @Date: 2024/6/26 17:07
 **/
@Slf4j
@Component
public class UsernameAuthenticationProvider implements AuthenticationProvider {

    @Resource
    private UserService userService;

    @Resource
    private PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        // 用户提交的用户名 + 密码：
        String username = (String) authentication.getPrincipal();
        String password = (String) authentication.getCredentials();
        UserInfo userInfo = userService.getUserFromDB(username);
        if (userInfo == null
                || !passwordEncoder.matches(password, userInfo.getPassword())) {

            throw new BusinessException(UNAUTHORIZED.getCode(), "用户名或密码不正确");
        }
        UsernameAuthentication token = new UsernameAuthentication(null);
        UserLoginInfo userLoginInfo = new UserLoginInfo();
        BeanUtils.copyProperties(userInfo, userLoginInfo);
        token.setCurrentUser(userLoginInfo);
        token.setAuthenticated(true); // 认证通过，这里一定要设成true
        return token;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.isAssignableFrom(UsernameAuthentication.class);
    }
}
