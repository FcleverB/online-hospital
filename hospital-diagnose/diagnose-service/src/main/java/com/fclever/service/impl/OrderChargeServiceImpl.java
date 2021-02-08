package com.fclever.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fclever.constants.Constants;
import com.fclever.domain.CareOrderItem;
import com.fclever.domain.OrderCharge;
import com.fclever.domain.OrderChargeItem;
import com.fclever.dto.OrderChargeDto;
import com.fclever.dto.OrderChargeFormDto;
import com.fclever.dto.OrderChargeItemDto;
import com.fclever.mapper.CareOrderItemMapper;
import com.fclever.mapper.OrderChargeItemMapper;
import com.fclever.mapper.OrderChargeMapper;
import com.fclever.service.OrderChargeService;
import com.fclever.vo.DataGridView;
import org.apache.dubbo.config.annotation.Method;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
@author Fclever
@create 2021-02-03 19:53
*/
@Service(methods = {@Method(name = "saveOrderChargeAndItems", retries = 1)})
public class OrderChargeServiceImpl implements OrderChargeService{

    @Autowired
    private OrderChargeMapper orderChargeMapper;

    @Autowired
    private OrderChargeItemMapper orderChargeItemMapper;

    @Autowired
    private CareOrderItemMapper careOrderItemMapper;

    /**
     * 创建订单并现金支付
     * @param orderChargeFormDto 待保存的支付信息数据
     */
    @Override
    public void saveOrderChargeAndItems(OrderChargeFormDto orderChargeFormDto) {
        // 获取主订单信息
        OrderChargeDto orderChargeDto = orderChargeFormDto.getOrderChargeDto();
        // 获取订单详情信息
        List<OrderChargeItemDto> orderChargeItemDtoList = orderChargeFormDto.getOrderChargeItemDtoList();
        /**
         * 设置主订单数据
         */
        // 值拷贝
        OrderCharge orderCharge = new OrderCharge();
        BeanUtil.copyProperties(orderChargeDto, orderCharge);
        // 给主订单信息其他信息赋值
        orderCharge.setOrderStatus(Constants.ORDER_STATUS_0); // 未支付，修改状态由别的方法来处理
        orderCharge.setCreateBy(orderChargeFormDto.getSimpleUser().getUserName());
        orderCharge.setCreateTime(DateUtil.date());
        // 执行保存方法
        this.orderChargeMapper.insert(orderCharge);
        /**
         * 设置订单详情信息
         */
        // 遍历处方详情集合，给其他信息赋值
        for (OrderChargeItemDto orderChargeItemDto : orderChargeItemDtoList) {
            // 值拷贝
            OrderChargeItem orderChargeItem = new OrderChargeItem();
            BeanUtil.copyProperties(orderChargeItemDto, orderChargeItem);
            orderChargeItem.setOrderId(orderCharge.getOrderId());
            orderChargeItem.setStatus(Constants.ORDER_DETAILS_STATUS_0); // 未支付，修改状态由别的方法来处理
            // 执行保存方法
            this.orderChargeItemMapper.insert(orderChargeItem);
        }
    }

    /**
     * 支付成功后更新订单状态
     * @param orderId 支付订单主表id
     * @param payPlatformId 支付平台id  这里是支付宝
     * @param payType   支付类型
     */
    @Override
    public void paySuccess(String orderId, String payPlatformId, String payType) {
        // 根据支付订单id查询支付订单主表数据
        OrderCharge orderCharge = this.orderChargeMapper.selectById(orderId);
        // 修改状态  支付成功
        orderCharge.setOrderStatus(Constants.ORDER_STATUS_1);
        orderCharge.setPayPlatformId(payPlatformId);
        orderCharge.setPayTime(DateUtil.date());
        orderCharge.setPayType(payType);
        this.orderChargeMapper.updateById(orderCharge);
        // 根据支付订单id查询对应订单详情集合信息
        QueryWrapper<OrderChargeItem> qw = new QueryWrapper<>();
        qw.eq(OrderChargeItem.COL_ORDER_ID, orderId);
        List<OrderChargeItem> orderChargeItemList = this.orderChargeItemMapper.selectList(qw);
        // 遍历修改状态，同时更新处方详情表中对应数据的状态
        for (OrderChargeItem orderChargeItem : orderChargeItemList) {
            // 修改状态支付成功
            orderChargeItem.setStatus(Constants.ORDER_DETAILS_STATUS_1);
            this.orderChargeItemMapper.updateById(orderChargeItem);
            // 设置对应的处方详情数据的状态为支付完成
            CareOrderItem careOrderItem = this.careOrderItemMapper.selectById(orderChargeItem.getItemId());
            careOrderItem.setStatus(Constants.ORDER_DETAILS_STATUS_1);
            this.careOrderItemMapper.updateById(careOrderItem);
        }
    }

    /**
     * 根据支付订单主表id查询订单信息（通过订单状态判断是否支付成功）
     * @param orderId   订单主表id
     * @return  订单信息
     */
    @Override
    public OrderCharge queryOrderChargeByOrderId(String orderId) {
        return this.orderChargeMapper.selectById(orderId);
    }

    /**
     * 分页查询所有支付订单信息
     * @param orderChargeDto    查询条件
     * @return  查询结果
     */
    @Override
    public DataGridView queryAllOrderChargeForPage(OrderChargeDto orderChargeDto) {
        // 创建分页对象
        Page<OrderCharge> page = new Page<>(orderChargeDto.getPageNum(), orderChargeDto.getPageSize());
        // 创建查询对象
        QueryWrapper<OrderCharge> qw = new QueryWrapper<>();
        // 封装查询条件
        qw.like(StringUtils.isNotBlank(orderChargeDto.getOrderId()), OrderCharge.COL_ORDER_ID, orderChargeDto.getOrderId());
        qw.like(StringUtils.isNotBlank(orderChargeDto.getPatientName()), OrderCharge.COL_PATIENT_NAME, orderChargeDto.getPatientName());
        qw.orderByDesc(OrderCharge.COL_CREATE_TIME);
        // 执行查询
        this.orderChargeMapper.selectPage(page, qw);
        return new DataGridView(page.getTotal(), page.getRecords());
    }
}
