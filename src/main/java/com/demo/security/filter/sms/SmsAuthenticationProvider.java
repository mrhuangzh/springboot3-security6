package com.demo.security.filter.sms;

import com.demo.security.entity.UserInfo;
import com.demo.security.entity.UserLoginInfo;
import com.demo.security.filter.username.UsernameAuthentication;
import com.demo.security.service.UserService;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

/**
 * @Author: mrhuangzh
 * @Date: 2024/6/27 23:10
 **/
@Component
public class SmsAuthenticationProvider implements AuthenticationProvider {

    @Resource
    private UserService userService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String phoneNumber = authentication.getPrincipal().toString();
        String captcha = authentication.getCredentials().toString();
        if ("000000".equals(captcha)) {
            UserInfo userInfo = userService.getUserByPhone(phoneNumber);

            if (userInfo == null) {
                // 密码错误，直接抛异常。
                // 根据SpringSecurity框架的代码逻辑，认证失败时，应该抛这个异常：org.springframework.security.core.AuthenticationException
                // BadCredentialsException 就是这个异常的子类
                throw new BadCredentialsException("用户不存在");
            }

            UsernameAuthentication token = new UsernameAuthentication(null);
            UserLoginInfo userLoginInfo = new UserLoginInfo();
            BeanUtils.copyProperties(userInfo, userLoginInfo);
            token.setCurrentUser(userLoginInfo);
            token.setAuthenticated(true); // 认证通过，这里一定要设成true
            return token;
        } else {
            throw new BadCredentialsException("验证码不正确");
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return SmsAuthentication.class.isAssignableFrom(authentication);
    }
}
