package com.fclever.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fclever.dto.OperationLogDto;
import com.fclever.vo.DataGridView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Arrays;
import java.util.List;
import com.fclever.domain.OperationLog;
import com.fclever.mapper.OperationLogMapper;
import com.fclever.service.OperationLogService;
/**
@author Fclever
@create 2020-11-16 08:32
*/
@Service
public class OperationLogServiceImpl implements OperationLogService{

    @Autowired
    private OperationLogMapper operationLogMapper;

    /**
     * 插入操作日志
     * @param operationLog 待插入的实体类
     */
    @Override
    public void insertOperationLog(OperationLog operationLog) {
        operationLogMapper.insert(operationLog);
    }

    /**
     * 分页查询操作日志
     * @param operationLogDto 查询条件
     * @return 分页数据
     */
    @Override
    public DataGridView listForPage(OperationLogDto operationLogDto) {
        // 分页对象
        Page<OperationLog> page = new Page<>(operationLogDto.getPageNum(), operationLogDto.getPageSize());
        // 查询条件对象
        QueryWrapper<OperationLog> qw = new QueryWrapper<>();
        // 封装查询条件
        // 模糊查询操作人员名称
        qw.like(StringUtils.isNotBlank(operationLogDto.getOperName()), OperationLog.COL_OPER_NAME, operationLogDto.getOperName());
        // 模糊查询模块标题
        qw.like(StringUtils.isNotBlank(operationLogDto.getTitle()), OperationLog.COL_TITLE, operationLogDto.getTitle());
        // 精确匹配业务类型   增删改  其他
        qw.eq(StringUtils.isNotBlank(operationLogDto.getBusinessType()), OperationLog.COL_BUSINESS_TYPE, operationLogDto.getBusinessType());
        // 精确匹配 操作状态  异常和正常
        qw.eq(StringUtils.isNotBlank(operationLogDto.getStatus()), OperationLog.COL_STATUS, operationLogDto.getStatus());
        // 范围匹配  开始时间《= 操作时间 《= 结束时间
        qw.ge(null != operationLogDto.getBeginTime(), OperationLog.COL_OPER_TIME, operationLogDto.getBeginTime());
        qw.le(null != operationLogDto.getEndTime(), OperationLog.COL_OPER_TIME, operationLogDto.getEndTime());
        // 根据操作时间降序排序
        qw.orderByDesc(OperationLog.COL_OPER_TIME);
        // 执行查询
        this.operationLogMapper.selectPage(page, qw);
        // 返回数据
        return new DataGridView(page.getTotal(), page.getRecords());
    }

    /**
     * 根据id删除操作日志（含批量）
     * @param operationIds
     * @return 是否操作成功的标志 0 失败，>0成功
     */
    @Override
    public int deleteOperationLogsByIds(Long[] operationIds) {
        if (null != operationIds && operationIds.length > 0){
            // 封装id数组为集合对象
            List<Long> ids = Arrays.asList(operationIds);
            // 执行批量删除方法
            return operationLogMapper.deleteBatchIds(ids);
        }
        return 0;
    }

    /**
     * 清空所有的操作日志
     * @return 是否操作成功的标志
     */
    @Override
    public int clearAllOperationLog() {
        // 执行删除操作，不传递条件表示为全删除
        return operationLogMapper.delete(null);
    }
}
