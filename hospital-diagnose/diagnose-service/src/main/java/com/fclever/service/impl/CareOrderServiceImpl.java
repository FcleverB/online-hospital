package com.fclever.service.impl;

import javax.annotation.Resource;
import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fclever.domain.CareOrder;
import com.fclever.mapper.CareOrderMapper;
import com.fclever.service.CareOrderService;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

/**
@author Fclever
@create 2021-01-20 20:42
*/
@Service
public class CareOrderServiceImpl implements CareOrderService{

    @Autowired
    private CareOrderMapper careOrderMapper;

    /**
     * 根据病历id查询处方列表数据
     * @param chId  病历id
     * @return  查询结果
     */
    @Override
    public List<CareOrder> queryCareOrdersByChId(String chId) {
        // 创建查询条件对象
        QueryWrapper<CareOrder> qw = new QueryWrapper<>();
        // 封装查询条件
        qw.eq(chId != null, CareOrder.COL_CH_ID, chId);
        // 执行查询返回结果
        return this.careOrderMapper.selectList(qw);
    }
}
