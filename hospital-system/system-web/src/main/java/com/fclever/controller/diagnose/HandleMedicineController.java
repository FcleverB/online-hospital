package com.fclever.controller.diagnose;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.fclever.aspectj.annotation.Log;
import com.fclever.aspectj.enums.BusinessType;
import com.fclever.constants.Constants;
import com.fclever.controller.BaseController;
import com.fclever.domain.CareHistory;
import com.fclever.domain.CareOrder;
import com.fclever.domain.CareOrderItem;
import com.fclever.service.CareHistoryService;
import com.fclever.service.CareOrderItemService;
import com.fclever.service.CareOrderService;
import com.fclever.vo.AjaxResult;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.*;

/**
 * 处方发药控制层
 * @author Fclever
 * @create 2021-02-11 09:35
 */
@RestController
@RequestMapping("doctor/handleMedicine")
public class HandleMedicineController extends BaseController {

    @Reference
    private CareHistoryService careHistoryService;

    @Reference
    private CareOrderService careOrderService;

    @Reference
    private CareOrderItemService careOrderItemService;

    /**
     * 处方发药
     * 符合条件
     * 1. 已支付的处方
     * 2. 类型为药品
     * 操作结果
     * 1. 已支付--》已完成
     * 2. 对应药品库存量减少对应数量
     * @return
     */
    @PostMapping("doMedicine")
    @HystrixCommand
    @Log(title = "处方发药", businessType = BusinessType.INSERT)
    public AjaxResult doMedicine(@RequestBody List<String> itemIds) {
        // 这里可以前端做校验，不选择内容点击按钮没操作即可
        if (itemIds == null || itemIds.isEmpty()) {
            return AjaxResult.fail("请选择要发药的药品项之后再操作！");
        }
        // 发药操作
        // 1、药品库存够，直接发药
        // 2、药品库存不够，只发够的，不够的提示患者退费，并且将库存不够的药品信息返回
        String msg = this.careOrderService.doMedicine(itemIds);
        if (StringUtils.isBlank(msg)) {
            return AjaxResult.success();
        } else {
            return AjaxResult.fail(msg);
        }
    }

    /**
     * 根据挂号单据id查询病历信息|处方信息|处方详情信息（已支付的药品项）
     * @param registrationId
     * @return
     */
    @GetMapping("getChargedCareHistoryOnlyMedicinesByRegistrationId/{registrationId}")
    @HystrixCommand
    public AjaxResult getChargedCareHistoryOnlyMedicinesByRegistrationId(@PathVariable String registrationId) {
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
            return AjaxResult.fail("该挂号单没有药品处方信息，请核对后再查询！");
        }
        for (CareOrder careOrder : careOrderList) {
            // 只需要药用处方
            if (careOrder.getCoType().equals(Constants.ITEM_TYPE_MEDICINES)) {
                // 将处方实体转换成Map类型
                Map<String, Object> beanToMap = BeanUtil.beanToMap(careOrder);
                beanToMap.put("careOrderItems", Collections.EMPTY_LIST);
                // 保存总价
                BigDecimal allAmount = new BigDecimal("0");
                // 根据处方id查询对应的已支付的处方详情列表
                List<CareOrderItem> careOrderItemList = this.careOrderItemService.queryCareOrderItemsChargedByCoId(careOrder.getCoId());
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
        }
        if (mapList.isEmpty()){
            return AjaxResult.fail("该挂号单没有已支付的药品处方信息，请核对后再查询！");
        }
        res.put("careOrders", mapList);
        return AjaxResult.success(res);
    }
}
