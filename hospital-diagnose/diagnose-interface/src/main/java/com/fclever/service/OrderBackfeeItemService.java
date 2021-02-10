package com.fclever.service;

import com.fclever.domain.OrderBackfeeItem;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
@author Fclever
@create 2021-02-09 09:14
*/
public interface OrderBackfeeItemService{


    /**
     * 根据退费单主表id查询对应的详情信息
     * @param backId    退费单主表id
     * @return  查询结果
     */
    List<OrderBackfeeItem> queryOrderBackfeeItemByBackId(String backId);
}
