package com.fclever.service.impl;

import javax.annotation.Resource;
import java.util.List;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fclever.constants.Constants;
import com.fclever.domain.CareOrder;
import com.fclever.domain.CareOrderItem;
import com.fclever.dto.CareOrderDto;
import com.fclever.dto.CareOrderFormDto;
import com.fclever.dto.CareOrderItemDto;
import com.fclever.mapper.CareOrderItemMapper;
import com.fclever.mapper.CareOrderMapper;
import com.fclever.service.CareOrderService;
import com.fclever.utils.IdGeneratorSnowflake;
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

    @Autowired
    private CareOrderItemMapper careOrderItemMapper;

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

    /**
     * 保存处方和处方详情信息
     * @param careOrderFormDto  待保存的数据
     * @return  返回结果
     */
    @Override
    public int saveCareOrderAndItem(CareOrderFormDto careOrderFormDto) {
        /**
         * 保存处方信息
         */
        CareOrderDto careOrderDto = careOrderFormDto.getCareOrder();
        // 创建实体
        CareOrder careOrder=new CareOrder();
        // 值拷贝
        BeanUtil.copyProperties(careOrderDto,careOrder);
        // 赋值
        careOrder.setCreateBy(careOrderDto.getSimpleUser().getUserName());
        careOrder.setCreateTime(DateUtil.date());
        // 执行插入
        int i=this.careOrderMapper.insert(careOrder);
        /**
         * 保存处方详细信息
         */
        List<CareOrderItemDto> careOrderItems = careOrderFormDto.getCareOrderItems();
        //保存详情数据
        // 遍历集合
        for (CareOrderItemDto careOrderItemDto : careOrderItems) {
            // 创建处方详情实体
            CareOrderItem careOrderItem=new CareOrderItem();
            // 值拷贝
            BeanUtil.copyProperties(careOrderItemDto,careOrderItem);
            // 设值
            careOrderItem.setCoId(careOrder.getCoId());
            careOrderItem.setCreateTime(DateUtil.date());
            careOrderItem.setStatus(Constants.ORDER_DETAILS_STATUS_0);//未支付
            careOrderItem.setItemId(IdGeneratorSnowflake.generatorIdWithProfix(Constants.ID_PROFIX_ITEM));
            // 执行插入
            this.careOrderItemMapper.insert(careOrderItem);
        }
        return i;
    }
}
