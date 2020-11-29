package com.fclever.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fclever.domain.Role;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
@author Fclever
@create 2020-11-29 11:19
*/
public interface RoleMapper extends BaseMapper<Role> {

    /**
     * 根据角色id删除sys_role_menu的数据
     * @param roleIdsList 待删除的角色id
     */
    void deleteRoleMenuByRoleIds(@Param("roleIdsList") List<Long> roleIdsList);

    /**
     * 根据角色id删除sys_role_user的数据
     * @param roleIdsList 待删除的角色id
     */
    void deleteRoleUserByRoleIds(@Param("roleIdsList") List<Long> roleIdsList);

    /**
     * 保存角色和菜单的关联关系
     * @param roleId    角色id
     * @param menuId   菜单id集合
     * @return  是否保存成功信息
     */
    int saveRoleMenu(@Param("roleId") Long roleId, @Param("menuId") Long menuId);
}