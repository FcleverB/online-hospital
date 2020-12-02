package com.fclever.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fclever.domain.User;
import org.apache.ibatis.annotations.Param;

/**
@author Fclever
@create 2020-10-20 13:13
*/
public interface UserMapper extends BaseMapper<User> {

    /**
     * 保存用户和角色信息
     * @param userId 用户id
     * @param roleId 角色id数组
     */
    void saveUserRole(@Param("userId") Long userId, @Param("roleId") Long roleId);
}