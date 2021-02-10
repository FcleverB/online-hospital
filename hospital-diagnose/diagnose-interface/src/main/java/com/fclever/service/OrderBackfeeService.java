package com.fclever.service;

import com.fclever.domain.OrderBackfee;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fclever.dto.OrderBackfeeFormDto;

/**
@author Fclever
@create 2021-02-09 09:13
*/
public interface OrderBackfeeService{

    /**
     * 保存退费订单和详情信息
     * @param orderBackfeeFormDto   待保存的数据
     */
    void saveOrderBackfeeAndItem(OrderBackfeeFormDto orderBackfeeFormDto);

    /**
     * 退费成功更新订单状态
     * @param backId    退费订单主表id
     * @param backPlatformId 退费平台id
     * @param backType   退费类型
     */
    void backSuccess(String backId, String backPlatformId, String backType);
}
