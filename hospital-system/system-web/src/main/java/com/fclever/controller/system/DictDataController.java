package com.fclever.controller.system;

import com.fclever.aspectj.annotation.Log;
import com.fclever.aspectj.enums.BusinessType;
import com.fclever.domain.DictData;
import com.fclever.dto.DictDataDto;
import com.fclever.service.DictDataService;
import com.fclever.utils.ShiroSecurityUtils;
import com.fclever.vo.AjaxResult;
import com.fclever.vo.DataGridView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 字典数据控制器
 * @author Fclever
 * @create 2020-11-04 08:46
 */
@RestController
@RequestMapping("system/dict/data")
public class DictDataController {

    @Autowired
    private DictDataService dictDataService;

    /**
     * 分页查询
     * @param dictDataDto 分页查询参数
     * @return 消息+code+数据+total
     */
    @GetMapping("listForPage")
    public AjaxResult listForPage(DictDataDto dictDataDto){
        DataGridView dataGridView = this.dictDataService.listPage(dictDataDto);
        return AjaxResult.success("分页查询成功", dataGridView.getData(), dataGridView.getTotal());
    }

    /**
     * 添加字典数据
     * @param dictDataDto 待添加字典数据
     * @return 消息+code
     */
    @PostMapping("addDictData")
    @Log(title = "添加字典数据",businessType = BusinessType.INSERT)
    public AjaxResult addDictData(@Validated DictDataDto dictDataDto){
        // 设置登录用户信息
        dictDataDto.setSimpleUser(ShiroSecurityUtils.getCurrentSimpleUser());
        // 执行插入操作
        return AjaxResult.toAjax(this.dictDataService.insert(dictDataDto));
    }

    /**
     * 修改字典数据
     * @param dictDataDto 待修改的字典数据
     * @return 消息+code
     */
    @PutMapping("updateDictData")
    @Log(title = "修改字典数据",businessType = BusinessType.UPDATE)
    public AjaxResult updateDictData(@Validated DictDataDto dictDataDto){
        // 设置登录用户信息
        dictDataDto.setSimpleUser(ShiroSecurityUtils.getCurrentSimpleUser());
        // 执行插入操作
        return AjaxResult.toAjax(this.dictDataService.update(dictDataDto));
    }

    /**
     * 根据id查询一个字典数据
     * @param dictCode 字典主键id
     * @return 消息+code+数据
     */
    @GetMapping("getOne/{dictCode}")
    public AjaxResult getOne(@PathVariable @Validated @NotBlank(message = "字典数据主键ID不能为空")Long dictCode){
        return AjaxResult.success(this.dictDataService.selectDictDataById(dictCode));
    }

    /**
     * 根据id批量删除字典数据
     * @param dictCodeIds 待批量删除的id
     * @return 消息+code
     */
    @DeleteMapping("deleteDictDataByIds/{dictCodeIds}")
    @Log(title = "删除字典数据（含批量）",businessType = BusinessType.DELETE)
    public AjaxResult deleteDictDataByIds(@PathVariable @Validated @NotBlank(message = "删除Id不能为空")Long[] dictCodeIds){
        // 大于0，就表示成功
        return AjaxResult.toAjax(this.dictDataService.deleteDictDataByIds(dictCodeIds));
    }

    /**
     * 根据字典类型查询字典数据
     *
     * @param dictType 待查询的字典类型
     * @return 查询到的字典数据集合
     */
    @GetMapping("getDataByType/{dictType}")
    public AjaxResult getDataByType(@PathVariable @Validated @NotBlank(message = "字典类型不能为空") String dictType){
        List<DictData> dictDataList = this.dictDataService.selectDictDataByDictType(dictType);
        return AjaxResult.success("根据字典类型查询数据成功", dictDataList);
    }
}
