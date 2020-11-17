package com.fclever.controller.system;

import com.fclever.dto.OperationLogDto;
import com.fclever.service.OperationLogService;
import com.fclever.vo.AjaxResult;
import com.fclever.vo.DataGridView;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 操作日志管理的控制层
 * @author Fclever
 * @create 2020-11-17 13:22
 */
@RestController
@RequestMapping("system/operationLog")
public class OperationLogController {

    @Autowired
    private OperationLogService operationLogService;

    /**
     * 分页查询操作日志记录
     * @param operLogDto 封装的查询条件
     * @return 查询结果
     */
    @GetMapping("listForPage")
    public AjaxResult listForPage(OperationLogDto operLogDto){
        DataGridView list = operationLogService.listForPage(operLogDto);
        return AjaxResult.success("查询成功",list.getData(),list.getTotal());
    }

    /**
     * 根据id删除对应的操作日志信息（含批量）
     * @param operIds 待删除的id集合
     * @return 删除结果
     */
    @DeleteMapping("deleteOperationLogByIds/{operIds}")
    public AjaxResult deleteOperLogByIds(@PathVariable Long[] operIds){
        return AjaxResult.toAjax(this.operationLogService.deleteOperationLogsByIds(operIds));
    }

    /**
     * 清空操作日志表数据
     * @return 操作结果
     */
    @DeleteMapping("clearAllOperationLog")
    public AjaxResult clearAllOperLog(){
        return AjaxResult.toAjax(this.operationLogService.clearAllOperationLog());
    }
}
