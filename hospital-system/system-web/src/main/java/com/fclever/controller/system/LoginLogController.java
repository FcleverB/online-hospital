package com.fclever.controller.system;

import com.fclever.dto.LoginLogDto;
import com.fclever.dto.OperationLogDto;
import com.fclever.service.LoginLogService;
import com.fclever.service.OperationLogService;
import com.fclever.vo.AjaxResult;
import com.fclever.vo.DataGridView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 登录日志控制层
 * @author Fclever
 * @create 2020-11-19 08:46
 */
@RestController
@RequestMapping("system/loginLog")
public class LoginLogController {

    @Autowired
    private LoginLogService loginLogService;

    /**
     * 分页查询登录日志记录
     * @param loginLogDto 封装的查询条件
     * @return 查询结果
     */
    @GetMapping("listForPage")
    public AjaxResult listForPage(LoginLogDto loginLogDto){
        DataGridView list = loginLogService.listForPage(loginLogDto);
        return AjaxResult.success("查询成功",list.getData(),list.getTotal());
    }

    /**
     * 根据id删除对应的登录日志信息（含批量）
     * @param infoIds 待删除的id集合
     * @return 删除结果
     */
    @DeleteMapping("deleteLoginLogByIds/{infoIds}")
    public AjaxResult deleteOperLogByIds(@PathVariable Long[] infoIds){
        return AjaxResult.toAjax(this.loginLogService.deleteLoginLogByIds(infoIds));
    }

    /**
     * 清空操作日志表数据
     * @return 操作结果
     */
    @DeleteMapping("clearAllLoginLog")
    public AjaxResult clearAllLoginLog(){
        return AjaxResult.toAjax(this.loginLogService.clearAllLoginLog());
    }
}
