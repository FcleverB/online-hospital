package com.fclever.controller.system;

import com.fclever.aspectj.annotation.Log;
import com.fclever.aspectj.enums.BusinessType;
import com.fclever.dto.DictTypeDto;
import com.fclever.service.DictTypeService;
import com.fclever.utils.ShiroSecurityUtils;
import com.fclever.vo.AjaxResult;
import com.fclever.vo.DataGridView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * 字典类型控制器
 * @author Fclever
 * @create 2020-11-03 14:35
 */
@RestController
@RequestMapping("system/dict/type")
public class DictTypeController {

    @Autowired
    private DictTypeService dictTypeService;

    /**
     * 分页查询
     * @param dictTypeDto 分页查询参数
     * @return 消息+code+数据+total
     */
    @GetMapping("listForPage")
    public AjaxResult listForPage(DictTypeDto dictTypeDto){
        DataGridView dataGridView = this.dictTypeService.listPage(dictTypeDto);
        return AjaxResult.success("分页查询成功", dataGridView.getData(), dataGridView.getTotal());
    }

    /**
     * 添加字典类型
     * @param dictTypeDto 待添加字典类型
     * @return 消息+code
     */
    @PostMapping("addDictType")
    @Log(title = "添加字典类型",businessType = BusinessType.INSERT)
    public AjaxResult addDictType(@RequestBody @Validated DictTypeDto dictTypeDto){
        // 检查数据库中是否已经存在了待添加的字典类型数据
        // 返回true表示可以存在，不能操作
        if (dictTypeService.checkDictTypeUnique(dictTypeDto.getDictId(), dictTypeDto.getDictType())){
            return AjaxResult.fail("新增字典["+dictTypeDto.getDictName()+"]失败，字典类型已经存在");
        }
        // 设置登录用户信息
        dictTypeDto.setSimpleUser(ShiroSecurityUtils.getCurrentSimpleUser());
        // 执行插入操作
        return AjaxResult.toAjax(this.dictTypeService.insert(dictTypeDto));
    }

    /**
     * 修改字典类型
     * @param dictTypeDto 待修改的数据
     * @return 消息+code
     */
    @PutMapping("updateDictType")
    @Log(title = "更新字典类型数据",businessType = BusinessType.UPDATE)
    public AjaxResult updateDictType(@Validated DictTypeDto dictTypeDto){
        // 检查数据库中是否已经存在了待修改的字典类型数据
        // 返回true表示可以存在，不能操作
        if (dictTypeService.checkDictTypeUnique(dictTypeDto.getDictId(), dictTypeDto.getDictType())){
            return AjaxResult.fail("修改字典["+dictTypeDto.getDictName()+"]失败，字典类型已经存在");
        }
        // 设置登录用户信息
        dictTypeDto.setSimpleUser(ShiroSecurityUtils.getCurrentSimpleUser());
        // 执行插入操作
        return AjaxResult.toAjax(this.dictTypeService.update(dictTypeDto));
    }

    /**
     * 根据id查询一个字典类型数据
     * @param dictId 字典类型主键id
     * @return 消息+code+数据
     */
    @GetMapping("getOne/{dictId}")
    public AjaxResult getOne(@PathVariable @Validated @NotBlank(message = "字典主键ID不能为空")Long dictId){
        return AjaxResult.success(this.dictTypeService.selectDictTypeById(dictId));
    }

    /**
     * 根据id批量删除字典类型
     * @param dictIds 待批量删除的id
     * @return 消息+code
     */
    @DeleteMapping("deleteDictTypeByIds/{dictIds}")
    @Log(title = "删除字典类型数据（含批量）",businessType = BusinessType.DELETE)
    public AjaxResult deleteDictTypeByIds(@PathVariable @Validated @NotBlank(message = "删除Id不能为空")Long[] dictIds){
        // 大于0，就表示成功
        return AjaxResult.toAjax(this.dictTypeService.deleteDictTypeByIds(dictIds));
    }

    /**
     * 查询所有状态可用的字典类型
     * @return 消息+code+数据+总条数
     */
    @GetMapping("selectAllDictType")
    public AjaxResult selectAllDictType(){
        return AjaxResult.success(this.dictTypeService.list().getData());
    }

    /**
     * 同步字典数据到缓存（包括字典类型+对应类型数据），如果缓存原来有内容，则直接覆盖
     * @return
             */
    @GetMapping("dictCacheAsync")
    @Log(title = "同步字典类型到缓存",businessType = BusinessType.CACHEASYNC)
    public AjaxResult dictCacheAsync(){
        try{
            this.dictTypeService.dictCacheAsync();
            return AjaxResult.success();
        }catch (Exception e){
            e.printStackTrace();
            // 出现异常则返回错误信息
            return AjaxResult.error();
        }
    }
}
