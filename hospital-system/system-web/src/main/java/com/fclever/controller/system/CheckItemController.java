package com.fclever.controller.system;

import com.fclever.dto.CheckItemDto;
import com.fclever.service.CheckItemService;
import com.fclever.utils.ShiroSecurityUtils;
import com.fclever.vo.AjaxResult;
import com.fclever.vo.DataGridView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 检查项目控制层
 * @author Fclever
 * @create 2020-12-05 13:26
 */
@RestController
@RequestMapping("system/checkItem")
public class CheckItemController {

    @Autowired
    private CheckItemService checkItemService;

    /**
     * 修改检查项目
     * @param checkItemDto 待修改内容
     * @return 是否修改成功标志
     */
    @PutMapping("updateCheckItem")
    public AjaxResult updateCheckItem(@Validated CheckItemDto checkItemDto){
        // 获取登录用户作为更新人
        checkItemDto.setSimpleUser(ShiroSecurityUtils.getCurrentSimpleUser());
        return AjaxResult.toAjax(this.checkItemService.updateCheckItem(checkItemDto));
    }

    /**
     * 分页查询检查项目
     * @param checkItemDto 查询条件
     * @return 查询结果
     */
    @GetMapping("listCheckItemForPage")
    public AjaxResult listCheckItemForPage(CheckItemDto checkItemDto){
        DataGridView dataGridView = this.checkItemService.listCheckItemForPage(checkItemDto);
        return AjaxResult.success("分页数据查询成功",dataGridView.getData(),dataGridView.getTotal());
    }

    /**
     * 根据id删除检查项目（含批量）
     * @param checkItemIds  待删除的检查项目id
     * @return 是否删除成功标志
     */
    @DeleteMapping("deleteCheckItemByIds/{checkItemIds}")
    public AjaxResult deleteCheckItemByIds(@PathVariable Long[] checkItemIds){
        return AjaxResult.toAjax(this.checkItemService.deleteCheckItemByIds(checkItemIds));
    }

    /**
     * 根据id查询检查项目
     * @param checkItemId 待查询的检查项目id
     * @return 查询到的结果
     */
    @GetMapping("getCheckItemById/{checkItemId}")
    public AjaxResult getCheckItemById(@PathVariable Long checkItemId){
        return AjaxResult.success(this.checkItemService.getCheckItemById(checkItemId));
    }

    /**
     * 查询所有可用的检查项目
     * @return 查询结果
     */
    @GetMapping("selectAllCheckItem")
    public AjaxResult selectAllCheckItem(){
        return AjaxResult.success(this.checkItemService.selectAllCheckItem());
    }

    /**
     * 添加检查项目
     * @param checkItemDto  待添加的内容
     * @return 是否添加成功标志
     */
    @PostMapping("addCheckItem")
    public AjaxResult addCheckItem(@Validated CheckItemDto checkItemDto){
        // 获取登录用户作为更新人
        checkItemDto.setSimpleUser(ShiroSecurityUtils.getCurrentSimpleUser());
        return AjaxResult.toAjax(this.checkItemService.addCheckItem(checkItemDto));
    }
}
