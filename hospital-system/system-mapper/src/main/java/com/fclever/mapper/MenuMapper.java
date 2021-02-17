package com.fclever.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fclever.domain.Menu;
import org.apache.ibatis.annotations.Param;

import java.io.Serializable;
import java.util.List;

/**
 * @author Fclever
 * @create 2020-10-21 09:04
 */
public interface MenuMapper extends BaseMapper<Menu> {

    /**
     * 根据菜单id查询它的子菜单的个数
     * 按alt+enter  可以辅助生成xml中SQL，并且为形参添加注解
     *
     * @param menuId
     * @return 查询到的子菜单个数
     */
    Long queryChildCountByMenuId(@Param("menuId") Long menuId);

    /**
     * 根据角色id查询该角色已经分配的所有菜单id（仅仅查询子菜单id）
     *
     * @param roleId 角色id
     * @return 查询结果
     */
    List<Long> queryMenuIdsByRoleId(@Param("roleId") Long roleId);

    /**
     * 根据用户Id查询用户所拥有的菜单
     *
     * @param userId 用户Id
     * @return 菜单结果
     */
    List<Menu> selectMenuListByUserId(@Param("userId") Serializable userId);
}
