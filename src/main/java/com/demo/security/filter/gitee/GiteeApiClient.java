package com.demo.security.filter.gitee;

import com.alibaba.fastjson2.JSON;
import com.demo.security.enums.ResponseCodeEnum;
import com.demo.security.exception.BusinessException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: mrhuangzh
 * @Date: 2024/6/28 10:02
 **/
@Component
public class GiteeApiClient {

    private RestTemplate restTemplate = new RestTemplate();

    @Value("${login.gitee.clientId}")
    private String clientId;

    @Value("${login.gitee.clientSecret}")
    private String clientSecret;

    @Value("${login.gitee.redirectUri}")
    private String redirectUri;

    public String getTokenByCode(String code) {

        String urlTemplate = "https://gitee.com/oauth/token?grant_type=authorization_code&code=%s&client_id=%s&redirect_uri=%s&client_secret=%s";
        String url = String.format(urlTemplate, code, clientId, redirectUri, clientSecret);
        String responseJson = sendPostRequest(url, null);
        Map<String, Object> responseMap = JSON.parseObject(responseJson, HashMap.class);
        Object accessToken = responseMap.get("access_token");
        if (accessToken == null) {
            throw new BusinessException(ResponseCodeEnum.UNAUTHORIZED.getCode(), "获取GiteeToken失败！");
        }
        return accessToken.toString();
    }

    public Map<String, Object> getThirdUserInfo(String token) {

        // {"id":1483966,"login":"用户名"}
        String responseJson = sendGetRequest("https://gitee.com/api/v5/user?access_token=" + token);
        Map<String, Object> responseMap = JSON.parseObject(responseJson, HashMap.class);
        Object openId = responseMap.get("id");
        if (openId == null) {
            return null;
        }

        HashMap<String, Object> thirdUser = new HashMap<>();
        thirdUser.put("openId", openId);
        thirdUser.put("nickname", responseMap.get("login"));
        return thirdUser;
    }

    public String sendPostRequest(String url, Map<String, Object> body) {
        // 请求头
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        // 创建HttpEntity对象
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(body, headers);
        // 发送POST请求
        try {
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity,
                    String.class);
            // 返回响应体
            return response.getBody();
        } catch (HttpClientErrorException e) {
            return e.getResponseBodyAsString();
        }
    }

    public String sendGetRequest(String url) {
        // 请求头
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        // 创建HttpEntity对象
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(headers);
        // 发送GET请求
        try {
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, requestEntity,
                    String.class);
            // 返回响应体
            return response.getBody();
        } catch (HttpClientErrorException e) {
            return e.getResponseBodyAsString();
        }
    }
}
