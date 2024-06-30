package com.demo.security.config;

import com.demo.security.filter.gitee.GiteeAuthenticationFilter;
import com.demo.security.filter.gitee.GiteeAuthenticationProvider;
import com.demo.security.filter.jwt.MyJwtAuthenticationFilter;
import com.demo.security.filter.sms.SmsAuthenticationFilter;
import com.demo.security.filter.sms.SmsAuthenticationProvider;
import com.demo.security.handler.LoginFailHandler;
import com.demo.security.handler.LoginSuccessHandler;
import com.demo.security.handler.exception.CustomAuthenticationExceptionHandler;
import com.demo.security.handler.exception.CustomAuthorizationExceptionHandler;
import com.demo.security.handler.exception.CustomSecurityExceptionHandler;
import com.demo.security.service.JwtService;
import jakarta.annotation.Resource;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.context.SecurityContextHolderFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.util.List;

/**
 * security 设置
 * @Author: mrhuangzh
 * @Date: 2024/6/26 11:55
 **/
@Configuration
@EnableWebSecurity
public class CustomWebSecurityConfig {

    @Resource
    private ApplicationContext applicationContext;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * 禁用一些默认filter，在应用启动时，将不再初始化被禁用的组件到 filter chain 中
     *
     * @param httpSecurity
     * @throws Exception
     */
    private void commonFilter(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.formLogin(AbstractHttpConfigurer::disable);// 表单登录，默认自动生成
        httpSecurity.logout(AbstractHttpConfigurer::disable);// 登出
        httpSecurity.httpBasic(AbstractHttpConfigurer::disable);// 基础配置
        httpSecurity.sessionManagement(AbstractHttpConfigurer::disable);// 会话管理
        httpSecurity.csrf(AbstractHttpConfigurer::disable);// CSRF 保护，默认激活
        httpSecurity.requestCache(AbstractHttpConfigurer::disable);// 重定向，认证前访问A，将被转到认证页面，认证后重定向回A
        httpSecurity.anonymous(AbstractHttpConfigurer::disable);// 匿名用户默认不为 null，且拥有权限 ROLE_ANONYMOUS
        httpSecurity.exceptionHandling(exception ->
                exception.authenticationEntryPoint(new CustomAuthenticationExceptionHandler())
                        .accessDeniedHandler(new CustomAuthorizationExceptionHandler()));
        httpSecurity.addFilterBefore(new CustomSecurityExceptionHandler(), SecurityContextHolderFilter.class);
    }

    /**
     * 通过 controller 接口完成登录
     *
     * @param httpSecurity
     * @return
     * @throws Exception
     */
    @Bean
    public SecurityFilterChain controllerLoginFilter(HttpSecurity httpSecurity) throws Exception {
        commonFilter(httpSecurity);
        // securityMatcher 限定当前 filter 的作用域
        httpSecurity.securityMatcher("/auth/login/username/**")
                .authorizeHttpRequests(authorize -> authorize.anyRequest().permitAll());
        return httpSecurity.build();
    }

    /**
     * 通过 jwt 进行 api 鉴权
     *
     * @param httpSecurity
     * @return
     * @throws Exception
     */
    @Bean
    public SecurityFilterChain JwtApiFilterChain(HttpSecurity httpSecurity) throws Exception {
        commonFilter(httpSecurity);
        // securityMatcher 限定当前 filter 的作用域
        httpSecurity.securityMatcher("/test/**")
                .authorizeHttpRequests(authorize -> authorize.anyRequest().authenticated());
        httpSecurity.addFilterBefore(new MyJwtAuthenticationFilter(applicationContext.getBean(JwtService.class)),
                UsernamePasswordAuthenticationFilter.class);
        return httpSecurity.build();
    }

    /**
     * 通过 filter chain 完成短信登录
     *
     * @param httpSecurity
     * @return
     * @throws Exception
     */
    @Bean
    public SecurityFilterChain smsLoginFilter(HttpSecurity httpSecurity) throws Exception {
        commonFilter(httpSecurity);
        // securityMatcher 限定当前 filter 的作用域
        httpSecurity.securityMatcher("/auth/login/sms/**")
                .authorizeHttpRequests(authorize -> authorize.anyRequest().authenticated());
        SmsAuthenticationFilter smsLoginFilter = new SmsAuthenticationFilter(
                new AntPathRequestMatcher("/auth/login/sms/login", HttpMethod.POST.name()),
                new ProviderManager(List.of(applicationContext.getBean(SmsAuthenticationProvider.class))),
                applicationContext.getBean(LoginSuccessHandler.class),
                applicationContext.getBean(LoginFailHandler.class)
        );
        httpSecurity.addFilterBefore(smsLoginFilter, UsernamePasswordAuthenticationFilter.class);
        return httpSecurity.build();
    }

    /**
     * gitee 三方登录
     *
     * @param httpSecurity
     * @return
     * @throws Exception
     */
    @Bean
    public SecurityFilterChain giteeLoginFilter(HttpSecurity httpSecurity) throws Exception {
        commonFilter(httpSecurity);
        // 放行 /auth/login/gitee/config
        httpSecurity.authorizeHttpRequests(authorize ->
                authorize.requestMatchers("/auth/login/gitee/config").permitAll());
        // securityMatcher 限定当前 filter 的作用域
        httpSecurity.securityMatcher("/auth/login/gitee/**")
                .authorizeHttpRequests(authorize -> authorize.anyRequest().authenticated());
        GiteeAuthenticationFilter giteeLoginFilter = new GiteeAuthenticationFilter(
                new AntPathRequestMatcher("/auth/login/gitee/login", HttpMethod.POST.name()),
                new ProviderManager(List.of(applicationContext.getBean(GiteeAuthenticationProvider.class))),
                applicationContext.getBean(LoginSuccessHandler.class),
                applicationContext.getBean(LoginFailHandler.class)
        );
        httpSecurity.addFilterBefore(giteeLoginFilter, UsernamePasswordAuthenticationFilter.class);
        return httpSecurity.build();
    }

    /**
     * 默认放行接口
     *
     * @param httpSecurity
     * @return
     * @throws Exception
     */
    @Bean
    public SecurityFilterChain openFilterChain(HttpSecurity httpSecurity) throws Exception {
        commonFilter(httpSecurity);
        // securityMatcher 限定当前 filter 的作用域
        httpSecurity.securityMatcher(
                        "/public/**")
                .authorizeHttpRequests(authorize -> authorize.anyRequest().permitAll());
        return httpSecurity.build();
    }
}
