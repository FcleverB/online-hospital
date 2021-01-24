package com.fclever.service;

import com.fclever.domain.CareOrder;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
@author Fclever
@create 2021-01-20 20:42
*/
public interface CareOrderService{

    /**
     * 根据病历id查询对应的处方集合
     * @param chId  病历id
     * @return  返回结果
     */
    List<CareOrder> queryCareOrdersByChId(String chId);
}
