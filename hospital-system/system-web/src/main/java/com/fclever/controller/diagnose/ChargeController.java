package com.fclever.controller.diagnose;

import cn.hutool.core.bean.BeanUtil;
import com.fclever.controller.BaseController;
import com.fclever.domain.CareHistory;
import com.fclever.domain.CareOrder;
import com.fclever.domain.CareOrderItem;
import com.fclever.domain.Registration;
import com.fclever.service.CareHistoryService;
import com.fclever.service.CareOrderItemService;
import com.fclever.service.CareOrderService;
import com.fclever.service.RegistrationService;
import com.fclever.vo.AjaxResult;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.*;

/**
 * 收费管理控制层
 * @author Fclever
 * @create 2021-01-27 12:06
 */
@RestController
@RequestMapping("doctor/charge")
public class ChargeController extends BaseController {

    @Reference
    private RegistrationService registrationService;

    @Reference
    private CareHistoryService careHistoryService;

    @Reference
    private CareOrderService careOrderService;

    @Reference
    private CareOrderItemService careOrderItemService;

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
}
