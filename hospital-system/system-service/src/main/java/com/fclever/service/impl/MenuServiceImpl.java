package com.fclever.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.fclever.constants.Constants;
import com.fclever.domain.SimpleUser;
import com.fclever.dto.MenuDto;
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

    /**
     * 修改菜单信息
     * @param menuDto 待修改的数据
     * @return 修改是否成功标志
     */
    @Override
    public int updateMenu(MenuDto menuDto) {
        // 拷贝
        Menu menu = new Menu();
        BeanUtil.copyProperties(menuDto, menu);
        // 设置修改人
        menu.setUpdateBy(menuDto.getSimpleUser().getUserName());
        return this.menuMapper.updateById(menu);
    }

    /**
     * 查询所有菜单信息
     * @param menuDto 查询条件
     * @return 查询到的数据
     */
    @Override
    public List<Menu> listAllMenus(MenuDto menuDto) {
        // 构建查询对象
        QueryWrapper<Menu> qw = new QueryWrapper<>();
        // 封装查询条件
        qw.like(StringUtils.isNotBlank(menuDto.getMenuName()), Menu.COL_MENU_NAME, menuDto.getMenuName());
        qw.eq(StringUtils.isNotBlank(menuDto.getStatus()), Menu.COL_STATUS,menuDto.getStatus());
        // 执行查询并返回结果
        return this.menuMapper.selectList(qw);
    }

    /**
     * 根据id删除指定菜单信息
     * @param menuId 待删除的菜单id集合
     * @return 是否删除成功标志
     */
    @Override
    public int deleteMenuById(Long menuId) {
        // 删除菜单权限中间表sys_role_menu的数据【后续添加】
        return this.menuMapper.deleteById(menuId);
    }

    /**
     * 根据id查询菜单信息
     * @param menuId 待查询的菜单id
     * @return 查询到的菜单实体对象
     */
    @Override
    public Menu getMenuById(Long menuId) {
        return this.menuMapper.selectById(menuId);
    }

    /**
     * 添加菜单信息
     * @param menuDto 添加的数据
     * @return 是否添加成功标志
     */
    @Override
    public int addMenu(MenuDto menuDto) {
        Menu menu = new Menu();
        // 拷贝
        BeanUtil.copyProperties(menuDto, menu);
        // 设置创建人
        menu.setCreateBy(menuDto.getSimpleUser().getUserName());
        // 设置创建时间
        menu.setCreateTime(DateUtil.date());
        return this.menuMapper.insert(menu);
    }

    /**
     * 判断指定菜单id是否含有子节点
     * @param menuId
     * @return
     */
    @Override
    public boolean hasChildByMenuId(Long menuId) {
        Long count = this.menuMapper.queryChildCountByMenuId(menuId);
        return count > 0L ? true : false;
    }

    /**
     * 根据角色id查询该角色已经分配的所有菜单id（仅仅查询子菜单id）
     * @param roleId    角色id
     * @return  查询结果
     */
    @Override
    public List<Long> getMenuIdsByRoleId(Long roleId) {
        return this.menuMapper.queryMenuIdsByRoleId(roleId);
    }
}
