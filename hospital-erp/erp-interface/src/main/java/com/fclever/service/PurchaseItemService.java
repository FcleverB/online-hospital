package com.fclever.service;

import com.fclever.domain.PurchaseItem;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
@author Fclever
@create 2020-12-14 22:30
*/
public interface PurchaseItemService{

    /**
     * 根据入库单据id查询对应的入库单据详情信息
     * @param purchaseId    待查询的入库单据id
     * @return  查询结果
     */
    List<PurchaseItem> getPurchaseItemById(String purchaseId);
}
