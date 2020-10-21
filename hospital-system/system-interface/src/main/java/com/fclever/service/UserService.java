package com.fclever.service;

import com.fclever.domain.User;
import com.baomidou.mybatisplus.extension.service.IService;
/**
@author Fclever
@create 2020-10-20 13:13
*/
public interface UserService{

    /**
     * 根据手机号查询用户
     *      用户登录是通过手机号和密码
     *      shiro验证：先通过手机号判断用户是否存在，如果存在则继续判断密码正确与否
     * @param phone
     * @return
     */
    User queryUserByPhone(String phone);

    /**
     * 根据用户id查询用户
     *      登录之后需要获取用户相关信息，基于token，也就需要用id来查询
     * @param userId
     * @return
     */
    User getOne(Long userId);
}
