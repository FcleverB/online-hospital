package com.fclever.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fclever.constants.Constants;
import com.fclever.domain.*;
import com.fclever.dto.*;
import com.fclever.mapper.*;
import com.fclever.service.OrderBackfeeService;
import com.fclever.vo.DataGridView;
import org.apache.dubbo.config.annotation.Method;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
@author Fclever
@create 2021-02-09 09:13
*/
@Service(methods = {@Method(name = "saveOrderBackfeeAndItem", retries = 1),@Method(name = "backSuccess", retries = 1)})
public class OrderBackfeeServiceImpl implements OrderBackfeeService{

    @Autowired
    private OrderBackfeeMapper orderBackfeeMapper;

    @Autowired
    private OrderBackfeeItemMapper orderBackfeeItemMapper;

    @Autowired
    private OrderChargeMapper orderChargeMapper;

    @Autowired
    private OrderChargeItemMapper orderChargeItemMapper;

    @Autowired
    private CareOrderItemMapper careOrderItemMapper;

    /**
     * 保存退费订单和详情信息
     * @param orderBackfeeFormDto   待保存的数据
     */
    @Override
    public void saveOrderBackfeeAndItem(OrderBackfeeFormDto orderBackfeeFormDto) {
        // 获取主订单信息
        OrderBackfeeDto orderBackfeeDto = orderBackfeeFormDto.getOrderBackfeeDto();
        // 获取订单详情信息
        List<OrderBackfeeItemDto> orderBackfeeItemDtoList = orderBackfeeFormDto.getOrderBackfeeItemDtoList();
        /**
         * 设置主订单数据
         */
        // 值拷贝
        OrderBackfee orderBackfee = new OrderBackfee();
        BeanUtil.copyProperties(orderBackfeeDto, orderBackfee);
        // 给主订单信息其他信息赋值
        orderBackfee.setBackStatus(Constants.ORDER_BACKFEE_STATUS_0); // 未退费，修改状态由别的方法来处理
        orderBackfee.setCreateBy(orderBackfeeFormDto.getSimpleUser().getUserName());
        orderBackfee.setCreateTime(DateUtil.date());
        // 执行保存方法
        this.orderBackfeeMapper.insert(orderBackfee);
        /**
         * 设置订单详情信息
         */
        // 遍历处方详情集合，给其他信息赋值
        for (OrderBackfeeItemDto orderBackfeeItemDto : orderBackfeeItemDtoList) {
            // 值拷贝
            OrderBackfeeItem orderBackfeeItem = new OrderBackfeeItem();
            BeanUtil.copyProperties(orderBackfeeItemDto, orderBackfeeItem);
            orderBackfeeItem.setBackId(orderBackfee.getBackId());
            orderBackfeeItem.setStatus(Constants.ORDER_BACKFEE_STATUS_0); // 未退费，修改状态由别的方法来处理
            // 执行保存方法
            this.orderBackfeeItemMapper.insert(orderBackfeeItem);
        }
    }

    /**
     * 退费成功更新订单状态
     * @param backId    退费订单主表id
     * @param backPlatformId 退费平台id
     * @param backType   退费类型
     */
    @Override
    public void backSuccess(String backId, String backPlatformId, String backType) {
        // 根据退费订单id查询退费订单主表数据
        OrderBackfee orderBackfee = this.orderBackfeeMapper.selectById(backId);
        // 修改状态  退费成功
        orderBackfee.setBackStatus(Constants.ORDER_BACKFEE_STATUS_1);
        orderBackfee.setBackPlatformId(backPlatformId);
        orderBackfee.setBackTime(DateUtil.date());
        orderBackfee.setBackType(backType);
        this.orderBackfeeMapper.updateById(orderBackfee);
        // 根据退费订单id查询对应退费订单详情集合信息
        QueryWrapper<OrderBackfeeItem> qw = new QueryWrapper<>();
        qw.eq(OrderBackfeeItem.COL_BACK_ID, backId);
        List<OrderBackfeeItem> orderBackfeeItems = this.orderBackfeeItemMapper.selectList(qw);
        // 遍历修改状态，同时更新处方详情表中对应数据的状态
        for (OrderBackfeeItem orderBackfeeItem : orderBackfeeItems) {
            // 修改退费详情订单状态已退费
            orderBackfeeItem.setStatus(Constants.ORDER_DETAILS_STATUS_2);
            this.orderBackfeeItemMapper.updateById(orderBackfeeItem);
            // 设置对应的处方详情数据的状态为支付完成
            CareOrderItem careOrderItem = this.careOrderItemMapper.selectById(orderBackfeeItem.getItemId());
            careOrderItem.setStatus(Constants.ORDER_DETAILS_STATUS_2);
            careOrderItem.setBackType(backType);
            this.careOrderItemMapper.updateById(careOrderItem);
            // 更新收费单状态为已退费
            OrderChargeItem orderChargeItem = this.orderChargeItemMapper.selectById(orderBackfeeItem.getItemId());
            orderChargeItem.setStatus(Constants.ORDER_DETAILS_STATUS_2);
            this.orderChargeItemMapper.updateById(orderChargeItem);
        }
    }

    /**
     * 分页查询所有退费订单信息
     * @param orderBackfeeDto   查询条件
     * @return  返回结果
     */
    @Override
    public DataGridView queryAllOrderBackfeeForPage(OrderBackfeeDto orderBackfeeDto) {
        Page<OrderBackfee> page = new Page<>(orderBackfeeDto.getPageNum(), orderBackfeeDto.getPageSize());
        QueryWrapper<OrderBackfee> qw = new QueryWrapper<>();
        qw.like(StringUtils.isNotBlank(orderBackfeeDto.getBackId()), OrderBackfee.COL_BACK_ID, orderBackfeeDto.getBackId());
        qw.eq(StringUtils.isNotBlank(orderBackfeeDto.getPatientName()), OrderBackfee.COL_PATIENT_NAME, orderBackfeeDto.getPatientName());
        qw.orderByDesc(OrderBackfee.COL_CREATE_TIME);
        // 执行查询
        this.orderBackfeeMapper.selectPage(page, qw);
        return new DataGridView(page.getTotal(), page.getRecords());
    }
}
