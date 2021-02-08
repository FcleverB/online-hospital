package com.fclever.service;

import com.fclever.domain.OrderChargeItem;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
@author Fclever
@create 2021-02-03 19:53
*/
public interface OrderChargeItemService{

    /**
     * 根据支付订单id查询对应的支付订单详情信息
     * @param orderId   支付订单id
     * @return  查询结果
     */
    List<OrderChargeItem> queryOrderChargeItemByOrderId(String orderId);
}
