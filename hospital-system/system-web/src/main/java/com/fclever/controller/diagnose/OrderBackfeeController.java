package com.fclever.controller.diagnose;

import cn.hutool.core.bean.BeanUtil;
import com.fclever.aspectj.annotation.Log;
import com.fclever.aspectj.enums.BusinessType;
import com.fclever.constants.Constants;
import com.fclever.controller.BaseController;
import com.fclever.domain.CareHistory;
import com.fclever.domain.CareOrder;
import com.fclever.domain.CareOrderItem;
import com.fclever.domain.OrderChargeItem;
import com.fclever.dto.BaseDto;
import com.fclever.dto.OrderBackfeeFormDto;
import com.fclever.service.*;
import com.fclever.utils.IdGeneratorSnowflake;
import com.fclever.utils.ShiroSecurityUtils;
import com.fclever.vo.AjaxResult;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.util.*;

/**
 * 退费管理控制器
 * @author Fclever
 * @create 2021-02-08 16:30
 */
@RestController
@RequestMapping("doctor/backfee")
public class OrderBackfeeController extends BaseController {

    @Reference
    private CareHistoryService careHistoryService;

    @Reference
    private CareOrderService careOrderService;

    @Reference
    private CareOrderItemService careOrderItemService;

    @Reference
    private OrderChargeItemService orderChargeItemService;

    @Reference
    private OrderBackfeeService orderBackfeeService;

    /**
     * 根据挂号单id查询病历|已支付处方信息|处方详情信息
     * @param registrationId 挂号单据id
     * @return  返回结果
     */
    @GetMapping("getChargedAllCareByRegistrationId/{registrationId}")
    @HystrixCommand
    public AjaxResult getChargedAllCareByRegistrationId(@PathVariable String registrationId) {
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
            // 根据处方id查询对应的已支付的处方详情列表
            List<CareOrderItem> careOrderItemList = this.careOrderItemService.queryCareOrderItemsChargedByCoId(careOrder.getCoId());
            // 如果当前处方没有已支付的数据了，就结束当前循环
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
            return AjaxResult.fail("该挂号单没有已支付的处方信息，请核对后再查询！");
        }
        res.put("careOrders", mapList);
        return AjaxResult.success(res);
    }

    /**
     * 创建现金收费订单
     * @param orderBackfeeFormDto   待保存的退费主表数据和退费详情数据集
     * @return  返回结果
     */
    @PostMapping("createOrderBackfeeWithCash")
    @HystrixCommand
    @Log(title = "创建现金退费订单", businessType = BusinessType.INSERT)
    public AjaxResult createOrderBackfeeWithCash(@RequestBody @Validated OrderBackfeeFormDto orderBackfeeFormDto) {
        // 保存订单
        orderBackfeeFormDto.getOrderBackfeeDto().setBackType(Constants.PAY_TYPE_STATUS_0);
        orderBackfeeFormDto.setSimpleUser(ShiroSecurityUtils.getCurrentSimpleUser());
        // 生成退费单号
        String backId = IdGeneratorSnowflake.generatorIdWithProfix(Constants.ID_PROFIX_BACK);
        orderBackfeeFormDto.getOrderBackfeeDto().setBackId(backId);
        // 找到当前退费单之前收费单的Id
        String itemId = orderBackfeeFormDto.getOrderBackfeeItemDtoList().get(0).getItemId();
        OrderChargeItem orderChargeItem = this.orderChargeItemService.queryOrderChargeItemByItemId(itemId);
        orderBackfeeFormDto.getOrderBackfeeDto().setOrderId(orderChargeItem.getOrderId());
        this.orderBackfeeService.saveOrderBackfeeAndItem(orderBackfeeFormDto);
        // 更新订单状态
        this.orderBackfeeService.backSuccess(backId, null, Constants.PAY_TYPE_STATUS_0);
        return AjaxResult.success("创建现金退费订单成功");
    }
}
