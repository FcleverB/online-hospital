package com.fclever.controller.system;

import com.fclever.aspectj.annotation.Log;
import com.fclever.aspectj.enums.BusinessType;
import com.fclever.domain.Dept;
import com.fclever.dto.DeptDto;
import com.fclever.service.DeptService;
import com.fclever.utils.ShiroSecurityUtils;
import com.fclever.vo.AjaxResult;
import com.fclever.vo.DataGridView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 科室管理控制层
 * @author Fclever
 * @create 2020-11-22 12:29
 */
@RestController
@RequestMapping("system/dept")
public class DeptController {

    @Autowired
    private DeptService deptService;

    /**
     * 修改科室信息
     * @param deptDto 待修改的数据
     * @return 统一返回实体
     */
    @PutMapping("updateDept")
    @Log(title = "更新科室信息",businessType = BusinessType.UPDATE)
    public AjaxResult updateDept(@Validated DeptDto deptDto){
        // 这里设置的登录用户将作为更新者进行保存
        deptDto.setSimpleUser(ShiroSecurityUtils.getCurrentSimpleUser());
        return AjaxResult.toAjax(this.deptService.updateDept(deptDto));
    }

    /**
     * 分页查询科室信息
     * @param deptDto 查询条件
     * @return 统一返回实体
     */
    @GetMapping("listForPage")
    public AjaxResult listForPage(DeptDto deptDto){
        DataGridView list = this.deptService.listForPage(deptDto);
        return AjaxResult.success("查询成功",list.getData(),list.getTotal());
    }

    /**
     * 查询所有科室信息（有效）
     * @return 统一返回实体
     */
    @GetMapping("selectAllDept")
    public AjaxResult selectAllDept(){
        return AjaxResult.success(this.deptService.selectAllDept());
    }

    /**
     * 根据id删除科室信息(含批量）
     * @param deptIds 待删除的主键id
     * @return 统一返回实体
     */
    @DeleteMapping("deleteDeptByIds/{deptIds}")
    @Log(title = "根据id删除科室信息（含批量)",businessType = BusinessType.DELETE)
    public AjaxResult deleteDeptByIds(@PathVariable @Validated @NotNull(message = "要删除的id不能为空") Long[] deptIds){
        return AjaxResult.toAjax(this.deptService.deleteDeptByIds(deptIds));
    }

    /**
     * 根据id查询指定科室信息
     * @param deptId 指定科室的主键id
     * @return 统一返回实体
     */
    @GetMapping("getDeptById/{deptId}")
    public AjaxResult getDeptById(@PathVariable @Validated @NotNull(message = "科室id不能为空") Long deptId){
        return AjaxResult.success(this.deptService.getDeptById(deptId));
    }

    /**
     * 添加科室信息
     * @param deptDto 待添加的信息（仅保存前端传递的数据，额外的单独设置保存）
     * @return 统一返回实体
     */
    @PostMapping("addDept")
    @Log(title = "添加科室信息",businessType = BusinessType.INSERT)
    public AjaxResult addDept(@Validated DeptDto deptDto){
        // 获取当前登录用户信息，这里的登录用户作为创建者来保存
        deptDto.setSimpleUser(ShiroSecurityUtils.getCurrentSimpleUser());
        return AjaxResult.toAjax(this.deptService.addDept(deptDto));
    }
}
