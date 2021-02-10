package com.fclever.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fclever.domain.OrderBackfeeItem;
import com.fclever.mapper.OrderBackfeeItemMapper;
import com.fclever.service.OrderBackfeeItemService;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
@author Fclever
@create 2021-02-09 09:14
*/
@Service
public class OrderBackfeeItemServiceImpl implements OrderBackfeeItemService{

    @Autowired
    private OrderBackfeeItemMapper orderBackfeeItemMapper;

    /**
     * 根据退费单主表id查询对应的详情信息
     * @param backId    退费单主表id
     * @return  查询结果
     */
    @Override
    public List<OrderBackfeeItem> queryOrderBackfeeItemByBackId(String backId) {
        QueryWrapper<OrderBackfeeItem> qw = new QueryWrapper<>();
        qw.eq(OrderBackfeeItem.COL_BACK_ID, backId);
        return this.orderBackfeeItemMapper.selectList(qw);
    }
}
