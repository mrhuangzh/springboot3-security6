package com.demo.security.filter.sms;

import com.demo.security.entity.UserLoginInfo;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * @Author: mrhuangzh
 * @Date: 2024/6/27 23:02
 **/
@Setter
@Getter
@Slf4j
public class SmsAuthentication extends AbstractAuthenticationToken {

    private String phone;

    private String captcha;
    private UserLoginInfo currentUser;

    public SmsAuthentication(Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
    }

    @Override
    public Object getCredentials() {
        return isAuthenticated() ? null : captcha;
    }

    @Override
    public Object getPrincipal() {
        return isAuthenticated() ? currentUser : phone;
    }

}
