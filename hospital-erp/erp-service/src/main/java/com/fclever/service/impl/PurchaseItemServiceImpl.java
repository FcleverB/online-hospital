package com.fclever.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.List;
import com.fclever.domain.PurchaseItem;
import com.fclever.mapper.PurchaseItemMapper;
import com.fclever.service.PurchaseItemService;
/**
@author Fclever
@create 2020-12-14 22:30
*/
@Service
public class PurchaseItemServiceImpl implements PurchaseItemService{

    @Autowired
    private PurchaseItemMapper purchaseItemMapper;

    /**
     * 根据入库单据id查询单据详细信息
     * @param purchaseId    待查询的入库单据id
     * @return 查询结果
     */
    @Override
    public List<PurchaseItem> getPurchaseItemById(String purchaseId) {
        if (null != purchaseId) {
            // 入库单据表和入库单独详情表示一对多的关系
            QueryWrapper<PurchaseItem> qw = new QueryWrapper<>();
            qw.eq(PurchaseItem.COL_PURCHASE_ID, purchaseId);
            return this.purchaseItemMapper.selectList(qw);
        }
        return Collections.EMPTY_LIST;
    }
}
