package com.fclever.service.impl;

import com.fclever.constants.Constants;
import com.fclever.domain.CareOrderItem;
import com.fclever.domain.CheckResult;
import com.fclever.domain.OrderChargeItem;
import com.fclever.mapper.CareOrderItemMapper;
import com.fclever.mapper.CheckResultMapper;
import com.fclever.mapper.OrderChargeItemMapper;
import com.fclever.mapper.OrderChargeMapper;
import com.fclever.service.CareOrderItemService;
import com.fclever.service.CheckResultService;
import com.fclever.service.OrderChargeService;
import org.apache.dubbo.config.annotation.Method;
import org.apache.dubbo.config.annotation.Reference;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
@author Fclever
@create 2021-02-15 13:11
*/
@Service(methods = {@Method(name = "startCheck", retries = 1)})
public class CheckResultServiceImpl implements CheckResultService{

    @Autowired
    private CheckResultMapper checkResultMapper;

    @Autowired
    private OrderChargeItemMapper orderChargeItemMapper;

    @Autowired
    private CareOrderItemMapper careOrderItemMapper;

    /**
     * 开始检查的方法
     * @param checkResult   待保存的数据
     */
    @Override
    @Transactional
    public void startCheck(CheckResult checkResult) {
        this.checkResultMapper.insert(checkResult);
        // 更新收费详情状态 已完成
        OrderChargeItem orderChargeItem = new OrderChargeItem();
        orderChargeItem.setItemId(checkResult.getItemId());
        orderChargeItem.setStatus(Constants.ORDER_DETAILS_STATUS_3);
        this.orderChargeItemMapper.updateById(orderChargeItem);
        // 更新处方详情状态
        CareOrderItem careOrderItem = new CareOrderItem();
        careOrderItem.setItemId(checkResult.getItemId());
        careOrderItem.setStatus(Constants.ORDER_DETAILS_STATUS_3);
        this.careOrderItemMapper.updateById(careOrderItem);
    }
}
