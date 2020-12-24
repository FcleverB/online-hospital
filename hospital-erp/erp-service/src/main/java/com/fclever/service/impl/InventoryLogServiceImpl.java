package com.fclever.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fclever.domain.InventoryLog;
import com.fclever.dto.InventoryLogDto;
import com.fclever.mapper.InventoryLogMapper;
import com.fclever.service.InventoryLogService;
import com.fclever.vo.DataGridView;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Fclever
 * @create 2020-12-24 02:30
 */
@Service
public class InventoryLogServiceImpl implements InventoryLogService {

    @Autowired
    private InventoryLogMapper inventoryLogMapper;

    /**
     * 分页查询批次库存及价格
     * @param inventoryLogDto 查询条件
     * @return  查询结果
     */
    @Override
    public DataGridView listInventoryLogPage(InventoryLogDto inventoryLogDto) {
        // 创建分页对象
        Page<InventoryLog> page=new Page<>(inventoryLogDto.getPageNum(),inventoryLogDto.getPageSize());
        // 封装查询条件
        QueryWrapper<InventoryLog> qw=new QueryWrapper<>();
        // 模糊查询入库单据id
        qw.like(StringUtils.isNotBlank(inventoryLogDto.getPurchaseId()),InventoryLog.COL_PURCHASE_ID,inventoryLogDto.getPurchaseId());
        // 模糊查询药品名称
        qw.like(StringUtils.isNotBlank(inventoryLogDto.getMedicinesName()),InventoryLog.COL_MEDICINES_NAME,inventoryLogDto.getMedicinesName());
        // 精确查询药品类型
        qw.eq(StringUtils.isNotBlank(inventoryLogDto.getMedicinesType()),InventoryLog.COL_MEDICINES_TYPE,inventoryLogDto.getMedicinesType());
        // 精确查询处方类型
        qw.eq(StringUtils.isNotBlank(inventoryLogDto.getPrescriptionType()),InventoryLog.COL_PRESCRIPTION_TYPE,inventoryLogDto.getPrescriptionType());
        // 精确查询生产厂家
        qw.eq(StringUtils.isNotBlank(inventoryLogDto.getProducterId()),InventoryLog.COL_PRODUCTER_ID,inventoryLogDto.getProducterId());
        // 创建时间范围匹配
        qw.ge(inventoryLogDto.getBeginTime()!=null,InventoryLog.COL_CREATE_TIME,inventoryLogDto.getBeginTime());
        qw.le(inventoryLogDto.getEndTime()!=null,InventoryLog.COL_CREATE_TIME,inventoryLogDto.getEndTime());
        // 降序排序创建时间
        qw.orderByDesc(InventoryLog.COL_CREATE_TIME);
        // 执行查询操作
        this.inventoryLogMapper.selectPage(page,qw);
        // 封装分页对象并返回
        return new DataGridView(page.getTotal(),page.getRecords());
    }
}
