package com.demo.security.exception;

/**
 * @Author: mrhuangzh
 * @Date: 2024/6/10 6:53
 **/
public class BaseException extends RuntimeException {
    private final int code;

    public BaseException(int code, String message) {
        super(message);
        this.code = code;
    }

    public BaseException(int code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
