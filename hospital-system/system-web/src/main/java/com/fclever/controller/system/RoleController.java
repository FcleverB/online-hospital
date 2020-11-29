package com.fclever.controller.system;

import com.fclever.domain.Role;
import com.fclever.dto.RoleDto;
import com.fclever.service.RoleService;
import com.fclever.utils.ShiroSecurityUtils;
import com.fclever.vo.AjaxResult;
import com.fclever.vo.DataGridView;
import org.aspectj.weaver.loadtime.Aj;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 角色管理控制层
 * @author Fclever
 * @create 2020-11-29 13:55
 */
@RestController
@RequestMapping("system/role")
public class RoleController {

    @Autowired
    private RoleService roleService;

    /**
     * 分页查询角色信息
     * @param roleDto   查询条件
     * @return 返回数据
     */
    @GetMapping("listRoleForPage")
    public AjaxResult listRoleForPage(RoleDto roleDto){
        DataGridView list = this.roleService.listRoleForPage(roleDto);
        return AjaxResult.success("分页查询成功", list.getData(),list.getTotal());
    }

    /**
     * 添加角色信息
     * @param roleDto 添加的数据
     * @return 是否插入成功标志
     */
    @PostMapping("addRole")
    public AjaxResult addRole(@Validated RoleDto roleDto){
        roleDto.setSimpleUser(ShiroSecurityUtils.getCurrentSimpleUser());
        return AjaxResult.toAjax(this.roleService.addRole(roleDto));
    }

    /**
     * 根据角色id查询对应角色信息
     * @param roleId 角色id
     * @return 查询到的数据
     */
    @GetMapping("getRoleById/{roleId}")
    public AjaxResult getRoleById(@PathVariable Long roleId){
        Role role = this.roleService.getRoleById(roleId);
        return AjaxResult.success(role);
    }

    /**
     * 更新角色信息
     * @param roleDto  待更新的数据
     * @return  是否更新成功的标志
     */
    @PutMapping("updateRole")
    public AjaxResult updateRole(@Validated RoleDto roleDto){
        roleDto.setSimpleUser(ShiroSecurityUtils.getCurrentSimpleUser());
        return AjaxResult.toAjax(this.roleService.updateRole(roleDto));
    }

    /**
     * 根据id删除角色信息（含批量）
     * @param roleIds   待删除的角色id集合
     * @return 是否删除成功标志
     */
    @DeleteMapping("deleteRoleByIds/{roleIds}")
    public AjaxResult deleteRoleByIds(@PathVariable Long[] roleIds){
        return AjaxResult.toAjax(this.roleService.deleteRoleByIds(roleIds));
    }

    /**
     * 查询所有可用状态的菜单
     * @return  查询结果
     */
    @GetMapping("selectAllRoles")
    public AjaxResult selectAllRoles(){
        List<Role> roles = this.roleService.selectAllRoles();
        return AjaxResult.success(roles);
    }

    /**
     * 保存角色和菜单的关联关系
     * @param roleId    角色id
     * @param menuIds   菜单id集合
     * @return  是否保存成功信息
     */
    @PostMapping("saveRoleAndMenu/{roleId}/{menuIds}")
    public AjaxResult saveRoleAndMenu(@PathVariable Long roleId, @PathVariable Long[] menuIds){
        /**
         * 使用路径传参，角色id一定不为空，因为是行数据传递过来的，但是menuIds可能为空
         * 前端对此进行了判断，如果是空的，传递过来的menuIds是【-1】的内容
         * 因此后端也要进行识别，这种情况表示该角色没有选择任何一个菜单
         */
        if (menuIds.length == 1 && menuIds[0].equals(1L)){
            menuIds = new Long[]{};
        }
        return AjaxResult.toAjax(this.roleService.saveRoleAndMenu(roleId,menuIds));
    }
}
