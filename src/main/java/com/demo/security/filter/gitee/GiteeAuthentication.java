package com.demo.security.filter.gitee;

import com.demo.security.entity.UserLoginInfo;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * @Author: mrhuangzh
 * @Date: 2024/6/28 9:54
 **/
@Setter
@Getter
public class GiteeAuthentication extends AbstractAuthenticationToken {
    private String giteeCode;
    private UserLoginInfo currentUser;

    public GiteeAuthentication(Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
    }

    @Override
    public Object getCredentials() {
        return isAuthenticated() ? null : giteeCode;
    }

    @Override
    public Object getPrincipal() {
        return isAuthenticated() ? currentUser : null;
    }
}
