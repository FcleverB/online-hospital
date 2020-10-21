package com.fclever.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fclever.constants.Constants;
import com.fclever.domain.SimpleUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fclever.domain.Menu;
import com.fclever.mapper.MenuMapper;
import com.fclever.service.MenuService;
/**
@author Fclever
@create 2020-10-21 09:04
*/
@Service
public class MenuServiceImpl implements MenuService{

    @Autowired
    private MenuMapper menuMapper;

    /**
     * 查询菜单信息
     *  超级管理员则查询所有
     *  如果是普通用户，则根据用户id查询对应菜单和权限
     * @param isAdmin 是否是超级管理员
     * @param  simpleUser  如果isAdmin=true  simpleUser可以为空
     * @return
     */
    @Override
    public List<Menu> selectMenuTree(boolean isAdmin, SimpleUser simpleUser) {
        QueryWrapper<Menu> qw=new QueryWrapper<>();
        // 菜单状态为正常（非停用）
        qw.eq(Menu.COL_STATUS, Constants.STATUS_TRUE);
        // 菜单类型为  一级菜单和二级菜单
        qw.in(Menu.COL_MENU_TYPE,Constants.MENU_TYPE_M,Constants.MENU_TYPE_C);
        // 根据父级菜单的id倒序排序
        qw.orderByAsc(Menu.COL_PARENT_ID);
        if(isAdmin){
            return menuMapper.selectList(qw);
        }else{
            //根据用户id查询用户拥有的菜单信息
            return menuMapper.selectList(qw);
        }
    }
}
