package com.fclever.service;

import com.fclever.domain.CareOrder;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fclever.dto.CareOrderFormDto;

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

    /**
     * 保存处方和处方详情信息
     * @param careOrderFormDto  待保存的数据
     * @return  返回结果
     */
    int saveCareOrderAndItem(CareOrderFormDto careOrderFormDto);
}
