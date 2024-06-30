package com.demo.security.response;

import com.demo.security.enums.ResponseCodeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @Author: mrhuangzh
 * @Date: 2024/6/7 0:21
 **/
@Data
@Builder
@Schema(title = "通用返回")
public class CommonResponse<T> implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(title = "状态码")
    private int code;
    @Schema(title = "返回信息")
    private String message;
    @Schema(title = "返回数据")
    private T data;
    @Schema(title = "全局链路请求id")
    private String rid;

    private CommonResponse() {
    }

    private CommonResponse(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    private CommonResponse(int code, String message, T data, String rid) {
        this.code = code;
        this.message = message;
        this.data = data;
        this.rid = rid;
    }

    /**
     * 自定义返回结果
     *
     * @param code
     * @param message
     * @param data
     * @param rid
     * @param <T>
     * @return
     */
    public static <T> CommonResponse<T> restResponse(int code, String message, T data, String rid) {
        return new CommonResponse<T>(code, message, data, rid);
    }

    /**
     * 成功返回结果
     */
    public static <T> CommonResponse<T> success() {
        return new CommonResponse<T>(ResponseCodeEnum.SUCCESS.getCode(), ResponseCodeEnum.SUCCESS.getMessage(), null);
    }

    /**
     * 成功返回结果
     *
     * @param data 获取的数据
     */
    public static <T> CommonResponse<T> success(T data) {
        return new CommonResponse<T>(ResponseCodeEnum.SUCCESS.getCode(), ResponseCodeEnum.SUCCESS.getMessage(), data);
    }

    /**
     * 成功返回结果
     *
     * @param data    获取的数据
     * @param message 提示信息
     */
    public static <T> CommonResponse<T> success(T data, String message) {
        return new CommonResponse<T>(ResponseCodeEnum.SUCCESS.getCode(), message, data);
    }

    /**
     * 失败返回结果
     */
    public static <T> CommonResponse<T> failed() {
        return failed(ResponseCodeEnum.FAILED);
    }

    /**
     * 失败返回结果
     *
     * @param errorCode 错误码
     */
    public static <T> CommonResponse<T> failed(ResponseCodeEnum errorCode) {
        return new CommonResponse<T>(errorCode.getCode(), errorCode.getMessage(), null);
    }

    /**
     * 失败返回结果
     *
     * @param message 提示信息
     */
    public static <T> CommonResponse<T> failed(String message) {
        return new CommonResponse<T>(ResponseCodeEnum.FAILED.getCode(), message, null);
    }

    /**
     * 失败返回结果
     *
     * @param errorCode 错误码
     * @param message   错误信息
     */
    public static <T> CommonResponse<T> failed(Integer errorCode, String message) {
        return new CommonResponse<T>(errorCode, message, null);
    }
}
