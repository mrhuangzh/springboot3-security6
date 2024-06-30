package com.demo.security.filter.gitee;

import com.alibaba.fastjson2.JSON;
import com.demo.security.entity.UserInfo;
import com.demo.security.entity.UserLoginInfo;
import com.demo.security.enums.ResponseCodeEnum;
import com.demo.security.exception.BusinessException;
import com.demo.security.service.UserService;
import jakarta.annotation.Resource;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: mrhuangzh
 * @Date: 2024/6/28 9:59
 **/
@Component
public class GiteeAuthenticationProvider implements AuthenticationProvider {

    @Resource
    private UserService userService;

    @Resource
    private GiteeApiClient giteeApiClient;


    @Override
    public Authentication authenticate(Authentication authentication) {
        try {
            String code = authentication.getCredentials().toString();
            String token = giteeApiClient.getTokenByCode(code);
            if (token == null) {
                // 乱传code过来。用户根本没授权！
                throw new BusinessException(ResponseCodeEnum.UNAUTHORIZED.getCode(), "Gitee授权失败！");
            }
            Map<String, Object> thirdUser = giteeApiClient.getThirdUserInfo(token);
            if (thirdUser == null) {
                // 未知异常。获取不到用户openId，也就无法继续登录了
                // 乱传code过来。用户根本没授权！
                throw new BusinessException(ResponseCodeEnum.UNAUTHORIZED.getCode(), "Gitee授权失败！");
            }
            String openId = thirdUser.get("openId").toString();

            // 通过第三方的账号唯一id，去匹配数据库中已有的账号信息
            UserInfo userInfo = userService.getUserByOpenId(openId, "gitee");
            boolean notBindAccount = userInfo == null; // gitee账号没有绑定我们系统的用户
            if (notBindAccount) {
                // 没找到账号信息，那就是第一次使用gitee登录，可能需要创建一个新用户
                userInfo = new UserInfo();
                userService.createUserWithOpenId(userInfo, openId, "gitee");
            }

            GiteeAuthentication successAuth = new GiteeAuthentication(null);
            successAuth.setCurrentUser(JSON.parseObject(JSON.toJSONString(userInfo), UserLoginInfo.class));
            successAuth.setAuthenticated(true); // 认证通过，一定要设成true

            HashMap<String, Object> loginDetail = new HashMap<>();
            // 第一次使用三方账号登录，需要告知前端，让前端跳转到初始化账号页面（可能需要）
            loginDetail.put("needInitUserInfo", notBindAccount);
            loginDetail.put("nickname", thirdUser.get("nickname").toString()); // sayHello
            successAuth.setDetails(loginDetail);

            return successAuth;
        } catch (Exception e) {
            throw new BusinessException(ResponseCodeEnum.UNAUTHORIZED.getCode(), "gitee 授权失败");
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return GiteeAuthentication.class.isAssignableFrom(authentication);
    }
}
