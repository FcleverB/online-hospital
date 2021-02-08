package com.fclever.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fclever.mapper.OrderChargeItemMapper;
import com.fclever.domain.OrderChargeItem;
import com.fclever.mapper.OrderChargeMapper;
import com.fclever.service.OrderChargeItemService;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
@author Fclever
@create 2021-02-03 19:53
*/
@Service
public class OrderChargeItemServiceImpl implements OrderChargeItemService{

    @Autowired
    private OrderChargeItemMapper orderChargeItemMapper;

    /**
     * 根据支付订单id查询对应的支付订单详情信息
     * @param orderId   支付订单id
     * @return  查询结果
     */
    @Override
    public List<OrderChargeItem> queryOrderChargeItemByOrderId(String orderId) {
        // 构建查询对象
        QueryWrapper<OrderChargeItem> qw = new QueryWrapper<>();
        // 封装查询条件
        qw.eq(orderId != null, OrderChargeItem.COL_ORDER_ID, orderId);
        // 执行查询返回结果
        return this.orderChargeItemMapper.selectList(qw);
    }
}
