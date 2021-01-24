package com.fclever.controller.system;

import com.fclever.aspectj.annotation.Log;
import com.fclever.aspectj.enums.BusinessType;
import com.fclever.dto.UserDto;
import com.fclever.service.UserService;
import com.fclever.utils.ShiroSecurityUtils;
import com.fclever.vo.AjaxResult;
import com.fclever.vo.DataGridView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 用户管理控制层
 * @author Fclever
 * @create 2020-11-30 18:14
 */
@RestController
@RequestMapping("system/user")
public class UserController {
    
    @Autowired
    private UserService userService;

    /**
     * 分页查询用户信息
     * @param userDto 查询条件
     * @return 结果
     */
    @GetMapping("listUserForPage")
    public AjaxResult listUserForPage(UserDto userDto){
        DataGridView list = this.userService.listUserForPage(userDto);
        return AjaxResult.success("分页查询用户信息成功", list.getData(), list.getTotal());
    }

    /**
     * 添加用户信息
     * @param userDto 待添加数据
     * @return 结果
     */
    @PostMapping("addUser")
    @Log(title = "添加用户信息",businessType = BusinessType.INSERT)
    public AjaxResult addUser(@RequestBody @Validated UserDto userDto){
        // 设置登录用户作为创建者
        userDto.setSimpleUser(ShiroSecurityUtils.getCurrentSimpleUser());
        return AjaxResult.toAjax(this.userService.addUser(userDto));
    }

    /**
     * 根据用户id查询用户
     *      登录之后需要获取用户相关信息，基于token，也就需要用id来查询
     * @param userId
     * @return 结果
     */
    @GetMapping("getUserById/{userId}")
    public AjaxResult getUserById(@PathVariable  @Validated @NotEmpty(message = "用户id不能为空") Long userId){
        return AjaxResult.success(this.userService.getUserById(userId));
    }

    /**
     * 修改用户信息
     * @param userDto 待修改数据
     * @return 结果
     */
    @PutMapping("updateUser")
    @Log(title = "修改用户信息",businessType = BusinessType.UPDATE)
    public AjaxResult updateUser(@Validated UserDto userDto){
        // 设置登录用户作为更新这
        userDto.setSimpleUser(ShiroSecurityUtils.getCurrentSimpleUser());
        return AjaxResult.toAjax(this.userService.updateUser(userDto));
    }

    /**
     * 根据id删除用户信息（含批量）
     * @param userIds 待删除的用户id数组
     * @return 结果
     */
    @DeleteMapping("deleteUserByIds/{userIds}")
    @Log(title = "删除用户信息（含批量）",businessType = BusinessType.DELETE)
    public AjaxResult deleteUserByIds(@PathVariable  @Validated @NotNull(message = "用户id不能为空") Long[] userIds){
        return AjaxResult.toAjax(this.userService.deleteUserByIds(userIds));
    }

    /**
     * 重置用户密码
     * @param userIds 待重置密码的用户id数组
     * @return 结果
     */
    @PutMapping("resetPassword/{userIds}")
    @Log(title = "重置用户密码",businessType = BusinessType.UPDATE)
    public AjaxResult resetPassword(@PathVariable @Validated @NotEmpty(message = "用户id不能为空") Long[] userIds){
        if (userIds.length > 0){
            return AjaxResult.toAjax(this.userService.resetPassword(userIds));
        }
        return AjaxResult.fail("重置密码失败，请至少选择一个用户");
    }

    /**
     * 查询所有用户信息（可用）
     * @return 结果
     */
    @GetMapping("selectAllUser")
    public AjaxResult selectAllUser(){
        return AjaxResult.success(this.userService.selectAllUser());
    }

    /**
     * 保存用户和角色信息
     * @param userId 用户id
     * @param roleIds 角色id数组
     * @return 操作结果
     */
    @PostMapping("saveUserAndRole/{userId}/{roleIds}")
    public AjaxResult saveUserAndRole(@PathVariable Long userId, @PathVariable Long[] roleIds){
        /**
         * 使用路径传参，角色id一定不为空，因为是行数据传递过来的，但是menuIds可能为空
         * 前端对此进行了判断，如果是空的，传递过来的menuIds是【-1】的内容
         * 因此后端也要进行识别，这种情况表示该角色没有选择任何一个菜单
         */
        if (roleIds.length == 1 && roleIds[0].equals(-1L)){
            roleIds = new Long[]{};
        }
        return AjaxResult.toAjax(this.userService.saveUserAndRole(userId, roleIds));
    }
}
