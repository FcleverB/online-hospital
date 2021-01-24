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
}
