package com.fclever.controller.system;

import com.fclever.dto.RegisteredItemDto;
import com.fclever.service.RegisteredItemService;
import com.fclever.utils.ShiroSecurityUtils;
import com.fclever.vo.AjaxResult;
import com.fclever.vo.DataGridView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 挂号费用控制层
 * @author Fclever
 * @create 2020-12-05 17:04
 */
@RestController
@RequestMapping("system/registeredItem")
public class RegisteredItemController {

    @Autowired
    private RegisteredItemService registeredItemService;

    /**
     * 修改挂号信息
     * @param registeredItemDto 待修改数据
     * @return 是否修改成功标志
     */
    @PutMapping("updateRegisteredItem")
    public AjaxResult updateRegisteredItem(@Validated RegisteredItemDto registeredItemDto){
        // 获取登录人为更新人
        registeredItemDto.setSimpleUser(ShiroSecurityUtils.getCurrentSimpleUser());
        return AjaxResult.toAjax(this.registeredItemService.updateRegisteredItem(registeredItemDto));
    }

    /**
     * 分页查询所有挂号信息
     * @param registeredItemDto 查询条件信息
     * @return 查询结果
     */
    @GetMapping("listRegisteredItemForPage")
    public AjaxResult listRegisteredItemForPage(RegisteredItemDto registeredItemDto){
        DataGridView dataGridView = this.registeredItemService.listRegisteredItemForPage(registeredItemDto);
        return AjaxResult.success("分页查询挂号费用信息成功",dataGridView.getData(),dataGridView.getTotal());
    }

    /**
     * 查询所有可用的挂号信息
     * @return 查询结果
     */
    @GetMapping("selectAllRegisteredItem")
    public AjaxResult selectAllRegisteredItem(){
        return AjaxResult.success(this.registeredItemService.selectAllRegisteredItem());
    }

    /**
     * 根据id删除对应挂号信息（含批量）
     * @param regItemId 待删除的挂号id
     * @return 是否删除成功的标志
     */
    @DeleteMapping("deleteRegisteredItemByIds/{regItemId}")
    public AjaxResult deleteRegisteredItemByIds(@PathVariable Long[] regItemId){
        return AjaxResult.toAjax(this.registeredItemService.deleteRegiteredItemByIds(regItemId));
    }

    /**
     * 根据id查询对应的挂号信息
     * @param regItemId 要查询的挂号id
     * @return 查询结果
     */
    @GetMapping("getRegisteredItemById/{regItemId}")
    public AjaxResult getRegisteredItemById(@PathVariable Long regItemId){
        return AjaxResult.success(this.registeredItemService.getRegisteredItemById(regItemId));
    }

    /**
     * 添加挂号费用信息
     * @param registeredItemDto 待添加的信息
     * @return 是否添加成功的标志
     */
    @PostMapping("addRegisteredItem")
    public AjaxResult addRegisteredItem(@Validated RegisteredItemDto registeredItemDto){
        registeredItemDto.setSimpleUser(ShiroSecurityUtils.getCurrentSimpleUser());
        return AjaxResult.toAjax(this.registeredItemService.addRegisteredItem(registeredItemDto));
    }
}
