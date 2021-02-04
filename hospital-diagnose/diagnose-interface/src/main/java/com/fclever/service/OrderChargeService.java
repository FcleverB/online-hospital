package com.fclever.service;

import com.fclever.domain.OrderCharge;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fclever.dto.OrderChargeFormDto;

/**
@author Fclever
@create 2021-02-03 19:53
*/
public interface OrderChargeService{

    /**
     * 保存支付订单信息及详情
     * @param orderChargeFormDto 待保存的支付信息数据
     */
    void saveOrderChargeAndItems(OrderChargeFormDto orderChargeFormDto);

    /**
     * 支付成功后更新订单状态
     * @param orderId 支付订单主表id
     * @param payPlatformId 支付平台id  这里是支付宝
     */
    void paySuccess(String orderId, String payPlatformId);
}
