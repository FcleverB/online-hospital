package com.fclever.service;

import com.fclever.domain.LoginLog;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fclever.dto.LoginLogDto;
import com.fclever.vo.DataGridView;

/**
@author Fclever
@create 2020-11-19 08:29
*/
public interface LoginLogService{

    /**
     *  插入登录日志  数据值并非页面传递
     * @param loginLog 登录日志对象
     * @return 是否插入成功标志
     */
    int insertLoginLog(LoginLog loginLog);

    /**
     * 分页查询登录日志信息
     * @param loginLogDto 封装查询条件
     * @return 分页对象，包含数据等
     */
    DataGridView listForPage(LoginLogDto loginLogDto);

    /**
     * 根据id删除登录日志信息（含批量）
     * @param infoIds 待删除的登录日志id
     * @return 是否删除成功标记
     */
    int deleteLoginLogByIds(Long[] infoIds);

    /**
     * 清空所有登录日志
     * @return 是否清空成功的标志
     */
    int clearAllLoginLog();
}
