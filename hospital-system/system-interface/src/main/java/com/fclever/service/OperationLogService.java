package com.fclever.service;

import com.fclever.domain.OperationLog;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fclever.dto.OperationLogDto;
import com.fclever.vo.DataGridView;

/**
@author Fclever
@create 2020-11-16 08:32
*/
public interface OperationLogService{

    /**
     * 插入操作日志
     * @param operationLog 待插入的实体类
     */
    void insertOperationLog(OperationLog operationLog);

    /**
     * 分页查询操作日志
     * @param operationLogDto 查询条件
     * @return 分页数据
     */
    DataGridView listForPage(OperationLogDto operationLogDto);

    /**
     * 根据id删除操作日志（含批量）
     * @param operationIds
     * @return 是否操作成功的标志
     */
    int deleteOperationLogsByIds(Long[] operationIds);

    /**
     * 清空所有的操作日志
     * @return 是否操作成功的标志
     */
    int clearAllOperationLog();

}
