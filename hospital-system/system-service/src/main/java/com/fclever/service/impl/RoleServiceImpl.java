package com.fclever.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fclever.constants.Constants;
import com.fclever.dto.RoleDto;
import com.fclever.vo.DataGridView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fclever.domain.Role;
import com.fclever.mapper.RoleMapper;
import com.fclever.service.RoleService;
/**
@author Fclever
@create 2020-11-29 11:19
*/
@Service
public class RoleServiceImpl implements RoleService{

    @Autowired
    private RoleMapper roleMapper;

    /**
     * 分页查询角色信息
     * @param roleDto   查询条件
     * @return 返回数据
     */
    @Override
    public DataGridView listRoleForPage(RoleDto roleDto) {
        // 创建分页对象
        Page<Role> page = new Page<>(roleDto.getPageNum(), roleDto.getPageSize());
        // 封装查询条件
        QueryWrapper<Role> qw = new QueryWrapper<>();
        // 执行查询，并封装返回数据到分页实体
        // 模糊查询角色名称
        qw.like(StringUtils.isNotBlank(roleDto.getRoleName()), Role.COL_ROLE_NAME, roleDto.getRoleName());
        // 模糊查询角色码值
        qw.like(StringUtils.isNotBlank(roleDto.getRoleCode()), Role.COL_ROLE_CODE, roleDto.getRoleCode());
        // 精确匹配状态
        qw.eq(StringUtils.isNotBlank(roleDto.getStatus()), Role.COL_STATUS, roleDto.getStatus());
        // 范围匹配创建时间
        qw.ge(roleDto.getBeginTime() != null, Role.COL_CREATE_TIME, roleDto.getBeginTime());
        qw.le(roleDto.getEndTime() != null, Role.COL_CREATE_TIME,roleDto.getEndTime());
        // 按显示顺序排序
        qw.orderByAsc(Role.COL_ROLE_SORT);
        // 返回
        this.roleMapper.selectPage(page, qw);
        return new DataGridView(page.getTotal(), page.getRecords());
    }

    /**
     * 添加角色信息
     * @param roleDto 添加的数据
     * @return 是否插入成功标志
     */
    @Override
    public int addRole(RoleDto roleDto) {
        // 创建Role对象，与数据库交互
        Role role = new Role();
        BeanUtil.copyProperties(roleDto, role);
        // 设置创建人和创建时间
        role.setCreateBy(roleDto.getSimpleUser().getUserName());
        role.setCreateTime(DateUtil.date());
        return this.roleMapper.insert(role);
    }

    /**
     * 根据角色id查询对应角色信息
     * @param roleId 角色id
     * @return 查询到的数据
     */
    @Override
    public Role getRoleById(Long roleId) {
        return this.roleMapper.selectById(roleId);
    }

    /**
     * 更新角色信息
     * @param roleDto  待更新的数据
     * @return  是否更新成功的标志
     */
    @Override
    public int updateRole(RoleDto roleDto) {
        // 创建Role实体  与数据库交互
        Role role = new Role();
        // 拷贝数据
        BeanUtil.copyProperties(roleDto, role);
        // 设置更新人
        role.setUpdateBy(roleDto.getSimpleUser().getUserName());
        return this.roleMapper.updateById(role);
    }

    /**
     * 根据id删除角色信息（含批量）
     *      一个用户可以有多个角色，一个角色可以对应多个菜单
     *       删除一个用户的话，对应的用户--角色表中的数据也要删除
     *       删除一个角色的话，对应的用户--角色表中对应用户下的该角色记录也要删除，对应的角色--菜单表中对应角色的记录也要删除
     *       删除一个菜单的话，对应的角色--菜单表中对应角色下的该菜单记录也要删除
     * @param roleIds   待删除的角色id集合
     * @return 是否删除成功标志
     */
    @Override
    public int deleteRoleByIds(Long[] roleIds) {
        List<Long> roleIdsList = Arrays.asList(roleIds);
        if (roleIdsList != null && roleIdsList.size() >0 ){
            // 根据角色id删除sys_role_menu的数据
            this.roleMapper.deleteRoleMenuByRoleIds(roleIdsList);
            // 根据角色id删除sys_role_user的数据
            this.roleMapper.deleteRoleUserByRoleIds(roleIdsList);
            // 删除角色表数据
            return this.roleMapper.deleteBatchIds(roleIdsList);
        }
        return 0;
    }

    /**
     * 查询所有可用状态的菜单
     * @return  查询结果
     */
    @Override
    public List<Role> selectAllRoles() {
        // 封装查询条件
        QueryWrapper<Role> qw = new QueryWrapper<>();
        // 精确匹配状态
        qw.eq(Role.COL_STATUS, Constants.STATUS_TRUE);
        // 返回
        return this.roleMapper.selectList(qw);
    }

    /**
     * 保存角色和菜单的关联关系
     * @param roleId    角色id
     * @param menuIds   菜单id集合
     * @return  是否保存成功信息
     */
    @Override
    public int saveRoleAndMenu(Long roleId, Long[] menuIds) {
        int row = 1;
        // 重新进行保存
        try{
            // 删除关联表中原来的sys_role_menu数据
            this.roleMapper.deleteRoleMenuByRoleIds(Arrays.asList(roleId));
            for (Long menuId : menuIds) {
                this.roleMapper.saveRoleMenu(roleId,menuId);
            }
        }catch (Exception e){
            return 0;
        }
        return row;
    }

    /**
     * 根据用户id查询该用户已经分配的所有角色id
     * @param userId    用户id
     * @return  查询结果
     */
    @Override
    public List<Long> getRoleIdsByUserId(Long userId) {
        return this.roleMapper.queryRoleIdsByUserId(userId);
    }
}
