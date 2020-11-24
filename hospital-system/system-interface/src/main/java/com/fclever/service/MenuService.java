package com.fclever.service;

import com.fclever.domain.Menu;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fclever.domain.SimpleUser;
import com.fclever.dto.MenuDto;
import com.fclever.vo.DataGridView;

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
    List<Menu> selectMenuTree(boolean isAdmin, SimpleUser simpleUser);

    /**
     * 修改菜单信息
     * @param menuDto 待修改的数据
     * @return 修改是否成功标志
     */
    int updateMenu(MenuDto menuDto);

    /**
     * 查询所有菜单信息
     * @param menuDto 查询条件
     * @return 查询到的数据
     */
    List<Menu> listAllMenus(MenuDto menuDto);

    /**
     * 根据id删除指定菜单信息
     * @param menuId 待删除的菜单id集合
     * @return 是否删除成功标志
     */
    int deleteMenuById(Long menuId);

    /**
     * 根据id查询菜单信息
     * @param menuId 待查询的菜单id
     * @return 查询到的菜单实体对象
     */
    Menu getMenuById(Long menuId);

    /**
     * 添加菜单信息
     * @param menuDto 添加的数据
     * @return 是否添加成功标志
     */
    int addMenu(MenuDto menuDto);

    /**
     * 判断指定菜单id是否含有子节点
     * @param menuId
     * @return
     */
    boolean hasChildByMenuId(Long menuId);
}
