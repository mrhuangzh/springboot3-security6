package com.demo.security.exception;

import com.demo.security.enums.ResponseCodeEnum;
import com.demo.security.exception.BusinessException;
import com.demo.security.response.CommonResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

/**
 * @Author: mrhuangzh
 * @Date: 2024/6/10 6:17
 **/
@Slf4j
@ControllerAdvice
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 处理所有不可知的异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(Throwable.class)
    public CommonResponse<Object> handleException(Throwable e) {
        log.error("请求发生错误，错误信息：", e);
        return CommonResponse.failed(ResponseCodeEnum.FAILED.getCode(), "服务器出错啦，请稍后再试哟～");
    }

    /**
     * 请求参数发生错误
     *
     * @param e
     * @return
     */
    @ExceptionHandler({HttpMessageNotReadableException.class,
            BindException.class,
            MethodArgumentNotValidException.class,
            ConstraintViolationException.class})
    public Object methodArgumentNotValidHandler(BindException e) {
        log.error("请求参数发生错误，错误信息：", e);
        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        StringBuilder errMsg = new StringBuilder();
        for (FieldError err : fieldErrors) {
            errMsg.insert(0, err.getDefaultMessage() + ";");
        }
        return CommonResponse.failed(ResponseCodeEnum.PARAM_ERROR.getCode(), errMsg.toString());
    }

    /**
     * 请求方式不被支持
     *
     * @param req
     * @return
     */
    @ExceptionHandler(value = HttpRequestMethodNotSupportedException.class)
    public CommonResponse<Object> httpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException req) {
        log.error("请求方式不被支持，错误信息：", req);
        return CommonResponse.failed(ResponseCodeEnum.REQUEST_METHOD_NOT_SUPPORT);
    }

    /**
     * 处理多种异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler({IllegalArgumentException.class,
            MissingServletRequestParameterException.class,
            HttpMediaTypeNotSupportedException.class,})
    public CommonResponse<Object> handleMultiException(Exception e) {
        log.error("请求发生错误，错误信息：", e);
        return CommonResponse.failed(ResponseCodeEnum.ERROR.getCode(), e.getMessage());
    }


    @ExceptionHandler(value = Exception.class)
    public CommonResponse<Object> customizeException(HttpServletRequest req, Exception e) {
        log.error("请求发生错误，错误信息：{}", e.getMessage());
        if (e instanceof BusinessException) {
            return CommonResponse.failed(((BusinessException) e).getCode(), e.getMessage());
        }
        return CommonResponse.failed(ResponseCodeEnum.SYSTEM_EXCEPTION);
    }
}
