package com.fclever.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    //  用户名
    private String username;
    //  密码
    private String password;
    // 验证码
    private String captcha;
}
