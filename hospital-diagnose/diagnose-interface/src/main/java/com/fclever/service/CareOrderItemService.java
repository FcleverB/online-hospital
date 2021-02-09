package com.fclever.service;

import com.fclever.domain.CareOrderItem;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
@author Fclever
@create 2021-01-20 20:43
*/
public interface CareOrderItemService{

    /**
     * 根据处方id查询对应的详细处方项目信息
     * @param coId  处方id
     * @return  返回结果
     */
    List<CareOrderItem> queryCareOrderItemsByCoId(String coId);

    /**
     * 根据处方项目id查询对应的处方项目
     * @param itemId    处方项目id
     * @return  返回结果
     */
    CareOrderItem queryCareOrderItemByItemId(String itemId);

    /**
     * 根据处方项目id删除对应的处方项目
     * @param itemId    待删除的处方项目
     * @return  返回结果
     */
    int deleteCareOrderItemByItemId(String itemId);

    /**
     * 根据处方id，查询未支付的处方详情信息
     * @param coId  处方id
     * @return  返回结果
     */
    List<CareOrderItem> queryCareOrderItemsNoChargeByCoId(String coId);

    /**
     * 根据处方id，查询已支付的处方详情信息
     * @param coId  处方id
     * @return  返回结果
     */
    List<CareOrderItem> queryCareOrderItemsChargedByCoId(String coId);
}
