package com.demo.security.filter.username;

import com.demo.security.entity.UserLoginInfo;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * @Author: mrhuangzh
 * @Date: 2024/6/26 17:17
 **/
@Setter
@Getter
@Slf4j
public class UsernameAuthentication extends AbstractAuthenticationToken {

    public UsernameAuthentication(Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
    }


    private String username;
    private String password;
    private UserLoginInfo currentUser; // 认证成功后，后台从数据库获取信息


    @Override
    public Object getCredentials() {
        // 根据SpringSecurity的设计，授权成后，Credential（比如，登录密码）信息需要被清空
        return isAuthenticated() ? null : password;
    }

    @Override
    public Object getPrincipal() {
        // 根据SpringSecurity的设计，授权成功之前，getPrincipal返回的客户端传过来的数据。授权成功后，返回当前登陆用户的信息
        return isAuthenticated() ? currentUser : username;
    }

}
