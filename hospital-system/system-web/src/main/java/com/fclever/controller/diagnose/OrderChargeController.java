package com.fclever.controller.diagnose;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.fclever.aspectj.annotation.Log;
import com.fclever.aspectj.enums.BusinessType;
import com.fclever.config.alipay.AlipayConfig;
import com.fclever.config.alipay.PayService;
import com.fclever.constants.Constants;
import com.fclever.controller.BaseController;
import com.fclever.domain.*;
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
        return AjaxResult.success("创建订单并现金支付成功");
    }

    /**
     * 创建订单并支付宝支付
     *      支付信息表his_order_charge\支付信息详情表his_order_charge_item\处方详情表his_care_order_item
     * @param orderChargeFormDto 待保存的支付信息和支付详情信息
     * @return  返回结果
     */
    @PostMapping("createOrderChargeWithZfb")
    @HystrixCommand
    @Log(title = "创建订单并支付宝支付", businessType = BusinessType.INSERT)
    public AjaxResult createOrderChargeWithZfb(@RequestBody @Validated OrderChargeFormDto orderChargeFormDto){
        // 保存支付订单和详情信息
        orderChargeFormDto.getOrderChargeDto().setPayType(Constants.PAY_TYPE_STATUS_1); // 支付类型，支付宝
        orderChargeFormDto.setSimpleUser(ShiroSecurityUtils.getCurrentSimpleUser());
        orderChargeFormDto.getOrderChargeDto().setOrderId(IdGeneratorSnowflake.generatorIdWithProfix(Constants.ID_PROFIX_ORDER));
        this.orderChargeService.saveOrderChargeAndItems(orderChargeFormDto);
        // 支付宝支付需要返回给页面一个二维码
        // 调用支付方法
        // (必填) 订单标题，粗略描述用户的支付目的。如“喜士多（浦东店）消费”
        String subject = "online-hospital系统处方收费";
        // (必填) 订单总金额，单位为元，不能超过1亿元
        String totalAmount = orderChargeFormDto.getOrderChargeDto().getOrderAmount().toString();
        // (必填) 商户网站订单系统中唯一订单号，64个字符以内，只能包含字母、数字、下划线，需保证商户系统端不能重复------对应支付订单信息表中的orderId即可
        // ！！！！每个订单号对应生成一个二维码，如果订单号一样，多次运行程序产生的二维码只能用一次
        String orderId = orderChargeFormDto.getOrderChargeDto().getOrderId();
        String outTradeNo = orderId;
        // (可选) 订单不可打折金额，可以配合商家平台配置折扣活动，如果酒水不参与打折，则将对应金额填写至此字段，如果该值未传入,但传入了【订单总金额】,【打折金额】,则该值默认为【订单总金额】-【打折金额】
        String undiscountableAmount = null;
        // 订单描述，可以对交易或商品进行一个详细地描述，比如填写"购买商品2件共15.00元"
        String body = "";
        List<OrderChargeItemDto> orderChargeItemDtoList = orderChargeFormDto.getOrderChargeItemDtoList();
        for (OrderChargeItemDto orderChargeItemDto : orderChargeItemDtoList) {
            body += orderChargeItemDto.getItemName() + "(" + orderChargeItemDto.getItemPrice()+"元) ";
        }
        // 支付宝服务器主动通知商户服务器里指定的页面http路径,根据需要设置
        String notifyUrl = AlipayConfig.notifyUrl + outTradeNo;
        Map<String, Object> result = PayService.pay(subject, totalAmount, outTradeNo, undiscountableAmount, body, notifyUrl);
        // 从支付成功信息中获取二维码
        String qrCode = result.get("qrCode").toString();
        if (StringUtils.isNotBlank(qrCode)) {
            // 支付成功，组装返回数据
            Map<String, Object> map = new HashMap<>();
            map.put("orderId", orderId);
            map.put("allAmount", totalAmount);
            map.put("payUrl", qrCode);
            return AjaxResult.success(map);
        } else {
            // 支付失败
            return AjaxResult.fail(result.get("msg").toString());
        }
    }

    /**
     * 根据支付订单主表id查询订单信息（通过订单状态判断是否支付成功）
     * @param orderId   订单主表id
     * @return  返回结果
     */
    @GetMapping("queryOrderChargeByOrderId/{orderId}")
    @HystrixCommand
    public AjaxResult queryOrderChargeByOrderId(@PathVariable String orderId) {
        OrderCharge orderCharge = this.orderChargeService.queryOrderChargeByOrderId(orderId);
        if (null == orderCharge) {
            return AjaxResult.fail("[" + orderId + "]订单号所对应的支付订单不存在，请核对后操作");
        }
        if (orderCharge.getPayType().equals(Constants.PAY_TYPE_STATUS_0)){
            return AjaxResult.fail("[" + orderId + "]订单号所对应的订单不是支付宝支付的订单，请核对后操作");
        }
        return AjaxResult.success(orderCharge);
    }
}
