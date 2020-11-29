package com.fclever.controller.system;

import com.fclever.aspectj.annotation.Log;
import com.fclever.aspectj.enums.BusinessType;
import com.fclever.constants.Constants;
import com.fclever.domain.Menu;
import com.fclever.dto.MenuDto;
import com.fclever.service.MenuService;
import com.fclever.utils.ShiroSecurityUtils;
import com.fclever.vo.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Fclever
 * @create 2020-11-24 07:58
 */
@RestController
@RequestMapping("system/menu")
public class MenuController {

    @Autowired
    private MenuService menuService;

    /**
     * 修改菜单信息
     * @param menuDto 待修改的数据
     * @return 修改是否成功标志
     */
    @PutMapping("updateMenu")
    @Log(title = "更新菜单信息",businessType = BusinessType.UPDATE)
    public AjaxResult updateMenu(@Validated MenuDto menuDto){
        // 这里设置的就是修改人了
        menuDto.setSimpleUser(ShiroSecurityUtils.getCurrentSimpleUser());
        return AjaxResult.toAjax(this.menuService.updateMenu(menuDto));
    }

    /**
     * 查询所有菜单信息
     * @param menuDto 查询条件
     * @return 查询到的数据
     */
    @GetMapping("listAllMenus")
    public AjaxResult listAllMenus(MenuDto menuDto){
        List<Menu> menus = this.menuService.listAllMenus(menuDto);
        return AjaxResult.success(menus);
    }

    /**
     * 查询菜单的下拉树
     * @return 查询的数据
     */
    @GetMapping("selectMenuTree")
    public AjaxResult selectMenuTree(){
        MenuDto menuDto = new MenuDto();
        // 只查询状态为可用的
        menuDto.setStatus(Constants.STATUS_TRUE);
        return this.listAllMenus(menuDto);
    }

    /**
     * 根据id删除指定菜单信息
     * @param menuId 待删除的菜单id集合
     * @return 是否删除成功标志
     */
    @DeleteMapping("deleteMenuById/{menuId}")
    @Log(title = "根据id删除菜单信息",businessType = BusinessType.DELETE)
    public AjaxResult deleteMenuById(@PathVariable Long menuId){
        // 删除前要判断当前删除菜单是否含有子节点，如果有不允许删除
        boolean hasChildren = this.menuService.hasChildByMenuId(menuId);
        if (hasChildren) {
            return AjaxResult.fail("当前要删除的菜单含有子菜单，请先删除子菜单后进行该操作！");
        }
        return AjaxResult.success(this.menuService.deleteMenuById(menuId));
    }

    /**
     * 根据id查询菜单信息
     * @param menuId 待查询的菜单id
     * @return 查询到的菜单实体对象
     */
    @GetMapping("getMenuById/{menuId}")
    public AjaxResult getMenuById(@PathVariable Long menuId){
        Menu menu = this.menuService.getMenuById(menuId);
        return AjaxResult.success(menu);
    }

    /**
     * 添加菜单信息
     * @param menuDto 添加的数据
     * @return 是否添加成功标志
     */
    @PostMapping("addMenu")
    @Log(title = "添加菜单信息",businessType = BusinessType.INSERT)
    public AjaxResult addMenu(@Validated MenuDto menuDto){
        // 保存创建者信息
        menuDto.setSimpleUser(ShiroSecurityUtils.getCurrentSimpleUser());
        return AjaxResult.toAjax(this.menuService.addMenu(menuDto));
    }

    /**
     * 根据角色id查询该角色已经分配的所有菜单id（仅仅查询子菜单id）
     * @param roleId    角色id
     * @return  查询结果
     */
    @GetMapping("getMenuIdsByRoleId/{roleId}")
    public AjaxResult getMenuIdsByRoleId(@PathVariable Long roleId){
        List<Long> menuIds = this.menuService.getMenuIdsByRoleId(roleId);
        return AjaxResult.success("查询对应角色的菜单权限成功",menuIds);
    }
}
