package com.fclever.controller.diagnose;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import com.fclever.aspectj.annotation.Log;
import com.fclever.aspectj.enums.BusinessType;
import com.fclever.constants.Constants;
import com.fclever.controller.BaseController;
import com.fclever.domain.CareHistory;
import com.fclever.domain.CareOrder;
import com.fclever.domain.CareOrderItem;
import com.fclever.domain.Registration;
import com.fclever.dto.OrderChargeDto;
import com.fclever.dto.OrderChargeFormDto;
import com.fclever.dto.OrderChargeItemDto;
import com.fclever.service.*;
import com.fclever.utils.IdGeneratorSnowflake;
import com.fclever.utils.ShiroSecurityUtils;
import com.fclever.vo.AjaxResult;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.math.BigDecimal;
import java.util.*;

/**
 * 收费管理控制层
 * @author Fclever
 * @create 2021-01-27 12:06
 */
@RestController
@RequestMapping("doctor/charge")
public class OrderChargeController extends BaseController {

    @Reference
    private RegistrationService registrationService;

    @Reference
    private CareHistoryService careHistoryService;

    @Reference
    private CareOrderService careOrderService;

    @Reference
    private CareOrderItemService careOrderItemService;

    @Reference
    private OrderChargeService orderChargeService;

    @Reference
    private OrderChargeItemService orderChargeItemService;

    /**
     * 根据挂号单id查询病历信息，未支付的处方和处方详情信息
     * @param registrationId
     * @return
     */
    @GetMapping("getNoChargeAllCareByRegistrationId/{registrationId}")
    @HystrixCommand
    public AjaxResult getNoChargeAllCareByRegistrationId(@PathVariable String registrationId) {
        // 根据挂号单id查询对应的病历信息--一个挂号单id对应一个病历信息
        CareHistory careHistory = this.careHistoryService.queryCareHistoryByRegistrationId(registrationId);
        if (careHistory == null) {
            return AjaxResult.fail("该挂号单没有对应的病历信息，请核对后再查询！");
        }
        // 构建返回的Map对象
        Map<String,Object> res = new HashMap<>();
        res.put("careHistory", careHistory);
        res.put("careOrders", Collections.EMPTY_LIST);
        // 声明一个可以存放careOrders结构
        List<Map<String, Object>> mapList = new ArrayList<>();
        // 根据病历id查询处方列表
        List<CareOrder> careOrderList = this.careOrderService.queryCareOrdersByChId(careHistory.getChId());
        // 处方列表可能为空
        if (careOrderList.isEmpty()) {
            // 如果没有处方信息
            return AjaxResult.fail("该挂号单没有处方信息，请核对后再查询！");
        }
        for (CareOrder careOrder : careOrderList) {
            // 将处方实体转换成Map类型
            Map<String, Object> beanToMap = BeanUtil.beanToMap(careOrder);
            beanToMap.put("careOrderItems", Collections.EMPTY_LIST);
            // 保存总价
            BigDecimal allAmount = new BigDecimal("0");
            // 根据处方id查询对应的未支付的处方详情列表
            List<CareOrderItem> careOrderItemList = this.careOrderItemService.queryCareOrderItemsNoChargeByCoId(careOrder.getCoId());
            // 如果当前处方没有未支付的数据了，就结束当前循环
            if (careOrderItemList.isEmpty()) {
                continue;
            } else {
                // 重新计算总价
                for (CareOrderItem careOrderItem : careOrderItemList) {
                    allAmount = allAmount.add(careOrderItem.getAmount());
                }
                beanToMap.put("careOrderItems", careOrderItemList);
                beanToMap.put("allAmount", allAmount);
                mapList.add(beanToMap);
            }
        }
        if (mapList.isEmpty()){
            return AjaxResult.fail("该挂号单没有未支付的处方信息，请核对后再查询！");
        }
        res.put("careOrders", mapList);
        return AjaxResult.success(res);
    }

    /**
     * 创建订单并现金支付
     *      支付信息表his_order_charge\支付信息详情表his_order_charge_item\处方详情表his_care_order_item
     * @param orderChargeFormDto 待保存的支付信息和支付详情信息
     * @return  返回结果
     */
    @PostMapping("createOrderChargeWithCash")
    @HystrixCommand
    @Log(title = "创建订单并现金支付", businessType = BusinessType.INSERT)
    public AjaxResult createOrderChargeWithCash(@RequestBody @Validated OrderChargeFormDto orderChargeFormDto){
        // 保存支付订单和详情信息
        orderChargeFormDto.getOrderChargeDto().setPayType(Constants.PAY_TYPE_STATUS_0); // 支付类型，现金支付
        orderChargeFormDto.setSimpleUser(ShiroSecurityUtils.getCurrentSimpleUser());
        orderChargeFormDto.getOrderChargeDto().setOrderId(IdGeneratorSnowflake.generatorIdWithProfix(Constants.ID_PROFIX_ORDER));
        this.orderChargeService.saveOrderChargeAndItems(orderChargeFormDto);
        // 现金支付直接更新支付详情的数据状态
        String orderId = orderChargeFormDto.getOrderChargeDto().getOrderId();
        this.orderChargeService.paySuccess(orderId, null);
        return AjaxResult.success("创建订单并现金支付");
    }
}
