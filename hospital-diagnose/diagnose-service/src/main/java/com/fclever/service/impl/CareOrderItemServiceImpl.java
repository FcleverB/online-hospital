package com.fclever.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import javax.annotation.Resource;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fclever.mapper.CareOrderItemMapper;
import com.fclever.domain.CareOrderItem;
import com.fclever.service.CareOrderItemService;
/**
@author Fclever
@create 2021-01-20 20:43
*/
@Service
public class CareOrderItemServiceImpl implements CareOrderItemService{

    @Autowired
    private CareOrderItemMapper careOrderItemMapper;

    /**
     * 根据处方id查询对应的详细处方项目信息
     * @param coId  处方id
     * @return  返回结果
     */
    @Override
    public List<CareOrderItem> queryCareOrderItemsByCoId(String coId) {
        // 构建查询条件对象
        QueryWrapper<CareOrderItem> qw = new QueryWrapper<>();
        // 封装查询条件
        qw.eq(coId !=null, CareOrderItem.COL_CO_ID, coId);
        // 执行查询返回结果
        return this.careOrderItemMapper.selectList(qw);
    }
}
