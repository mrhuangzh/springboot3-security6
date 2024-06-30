package com.demo.security.entity.param;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @Author: mrhuangzh
 * @Date: 2024/6/27 23:07
 **/
@Data
@Schema(title = "登录参数")
public class LoginParam {
    @Schema(title = "用户名")
    private String name;
    @Schema(title = "密码")
    private String password;
    @Schema(title = "手机号")
    private String phone;
    @Schema(title = "验证码")
    private String captcha;
    @Schema(title = "index.html获取giteeCode后，在gitee-callback.thml中调用gitee login接口时传入")
    private String giteeCode;
}
