package com.fclever.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fclever.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.fclever.domain.User;
import com.fclever.service.UserService;
/**
@author Fclever
@create 2020-10-20 13:13
*/
@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private UserMapper userMapper;

    /**
     * 根据手机号查询用户
     *      用户登录是通过手机号和密码
     *      shiro验证：先通过手机号判断用户是否存在，如果存在则继续判断密码正确与否
     * @param phone
     * @return
     */
    @Override
    public User queryUserByPhone(String phone) {
        QueryWrapper<User> qw = new QueryWrapper<>();
        qw.eq(User.COL_PHONE, phone);
        User user = this.userMapper.selectOne(qw);
        return user;
    }

    /**
     * 根据用户id查询用户
     *      登录之后需要获取用户相关信息，基于token，也就需要用id来查询
     * @param userId
     * @return
     */
    @Override
    public User getOne(Long userId) {
        return this.userMapper.selectById(userId);
    }
}
