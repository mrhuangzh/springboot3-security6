package com.demo.security.exception;

import com.demo.security.enums.ResponseCodeEnum;
import lombok.Getter;

/**
 * @Author: mrhuangzh
 * @Date: 2024/6/10 6:25
 **/
@Getter
public class BusinessException extends BaseException {


    public BusinessException(ResponseCodeEnum responseEnum) {
        super(responseEnum.getCode(), responseEnum.getMessage());
    }

    public BusinessException(String message) {
        super(ResponseCodeEnum.FAILED.getCode(), message);
    }

    public BusinessException(int code, String message) {
        super(code, message);
    }

    public BusinessException(int code, String message, Throwable cause) {
        super(code, message, cause);
    }

}
