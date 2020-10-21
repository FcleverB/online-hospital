package com.fclever.service;

import com.fclever.domain.Menu;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fclever.domain.SimpleUser;

import java.util.List;

/**
@author Fclever
@create 2020-10-21 09:04
*/
public interface MenuService{

    /**
     * 查询菜单信息
     *  sys_user表中user_type=0 超级管理员  user_type=1普通用户
     * 如查用户是超级管理员，那么查询所有菜单和权限
     * 如果用户是普通用户，那么根据用户ID关联角色和权限
     * @param isAdmin 是否是超级管理员
     * @param  simpleUser  如果isAdmin=true  simpleUser可以为空
     */
    public List<Menu> selectMenuTree(boolean isAdmin, SimpleUser simpleUser);
}
