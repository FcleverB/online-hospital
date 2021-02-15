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

    /**
     * 发药
     * 1、根据处方详情Id查询处方详情
     * 2、扣减药品库存（库存够-》扣减并返回影响行数；库存不够-》返回0）
     * 3、如果返回0，表示库存不够，停止发药
     * 4、如果返回大于0，更新处方详情和支付详情状态为3，已发药
     * @param itemIds   待发药的药品项Id
     * @return  返回结果
     */
    String doMedicine(List<String> itemIds);
}
