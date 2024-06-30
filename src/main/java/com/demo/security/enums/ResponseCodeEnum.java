package com.demo.security.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author: mrhuangzh
 * @Date: 2024/6/7 0:20
 **/
@Getter
@AllArgsConstructor
public enum ResponseCodeEnum {

    SUCCESS(20000, "请求成功"),

    UNAUTHORIZED(40001, "未认证"),
    FORBIDDEN(40003, "无权限"),
    REQUEST_METHOD_NOT_SUPPORT(40005, "请求方式不被支持"),

    FAILED(50000, "操作失败"),
    ERROR(50001, "请求发生错误"),
    PARAM_ERROR(50002, "请求参数错误"),
    DB_ERROR(50003, "数据库操作异常"),

    PARAM_NULL_ERROR(51001, "参数不可为空"),

    SYSTEM_EXCEPTION(99999, "系统异常"),

    ;


    private final Integer code;

    private final String message;
}
