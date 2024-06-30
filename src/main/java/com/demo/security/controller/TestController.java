package com.demo.security.controller;

import com.demo.security.response.CommonResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.util.Enumeration;

/**
 * @Author: mrhuangzh
 * @Date: 2024/6/26 11:39
 **/
@Slf4j
@RestController
@RequestMapping("/test")
@Tag(name = "test", description = "test controller")
public class TestController {

    @GetMapping("/method1")
    @Operation(summary = "method1", description = "get method1")
    public CommonResponse<Object> method1(HttpServletRequest request) throws UnknownHostException {
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            String headerValue = request.getHeader(headerName);
//            System.out.println(headerName + ": " + headerValue);
        }
        // 获取本地主机的InetAddress实例
        InetAddress localhost = InetAddress.getLocalHost();

        // 获取本地主机的IP地址
        String ipAddress = localhost.getHostAddress();
//        String info = "serverIP: " + ipAddress + ", serverPort: " + request.getServerPort() + ", method: " + request.getMethod() + ", method2";
//        String info = """
//                serverIP: %s, serverPort: %d, method: %s, method2
//                """.formatted(ipAddress, request.getServerPort(), request.getMethod());
        String info = String.format("serverIP: %s, serverPort: %d, method: %s, method2, time: %s",
                ipAddress, request.getServerPort(), request.getMethod(), LocalDateTime.now());
        return CommonResponse.success(info);
    }
}
