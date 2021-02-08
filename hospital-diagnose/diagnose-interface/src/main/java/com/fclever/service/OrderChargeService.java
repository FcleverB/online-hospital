package com.fclever.service;

import com.fclever.domain.OrderCharge;
import com.fclever.dto.OrderChargeDto;
import com.fclever.dto.OrderChargeFormDto;
import com.fclever.vo.DataGridView;

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
     * @param payType    支付类型
     */
    void paySuccess(String orderId, String payPlatformId, String payType);

    /**
     * 根据支付订单主表id查询订单信息（通过订单状态判断是否支付成功）
     * @param orderId   订单主表id
     * @return  订单信息
     */
    OrderCharge queryOrderChargeByOrderId(String orderId);

    /**
     * 分页查询所有支付订单信息
     * @param orderChargeDto    查询条件
     * @return  查询结果
     */
    DataGridView queryAllOrderChargeForPage(OrderChargeDto orderChargeDto);
}
