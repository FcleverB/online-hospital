package com.fclever.service;

import com.fclever.domain.Role;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fclever.dto.RoleDto;
import com.fclever.vo.DataGridView;

import java.util.List;

/**
@author Fclever
@create 2020-11-29 11:19
*/
public interface RoleService{

    /**
     * 分页查询角色信息
     * @param roleDto   查询条件
     * @return 返回数据
     */
    DataGridView listRoleForPage(RoleDto roleDto);

    /**
     * 添加角色信息
     * @param roleDto 添加的数据
     * @return 是否插入成功标志
     */
    int addRole(RoleDto roleDto);

    /**
     * 根据角色id查询对应角色信息
     * @param roleId 角色id
     * @return 查询到的数据
     */
    Role getRoleById(Long roleId);

    /**
     * 更新角色信息
     * @param roleDto  待更新的数据
     * @return  是否更新成功的标志
     */
    int updateRole(RoleDto roleDto);

    /**
     * 根据id删除角色信息（含批量）
     * @param roleIds   待删除的角色id集合
     * @return 是否删除成功标志
     */
    int deleteRoleByIds(Long[] roleIds);

    /**
     * 查询所有可用状态的菜单
     * @return  查询结果
     */
    List<Role> selectAllRoles();

    /**
     * 保存角色和菜单的关联关系
     * @param roleId    角色id
     * @param menuIds   菜单id集合
     * @return  是否保存成功信息
     */
    int saveRoleAndMenu(Long roleId, Long[] menuIds);

    /**
     * 根据用户id查询该用户已经分配的所有角色id
     * @param userId    用户id
     * @return  查询结果
     */
    List<Long> getRoleIdsByUserId(Long userId);
}
