package com.fclever.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 基础登录信息封装，保留前端登录时的一些信息，并可以做后盾数据校验
 * @author Fclever
 * @create 2020-10-20 08:12
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginBodyDto implements Serializable {
    //  用户名  手机号为登录用户名
    @NotNull(message = "用户名不能为空")
    private String username;
    //  密码
    @NotNull(message = "密码不能为空")
    private String password;
    // 验证码
    private String captcha;
}
