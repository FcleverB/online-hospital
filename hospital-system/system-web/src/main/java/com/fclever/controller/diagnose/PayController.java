package com.fclever.controller.diagnose;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.fclever.aspectj.annotation.Log;
import com.fclever.aspectj.enums.BusinessType;
import com.fclever.config.alipay.AlipayConfig;
import com.fclever.service.OrderChargeService;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * 支付成功后，支付宝回调请求映射处理
 * @author Fclever
 * @create 2021-02-04 23:32
 */
@RestController
@RequestMapping("pay")
@Log4j2
public class PayController {

    @Reference
    private OrderChargeService orderChargeService;

    /**
     * 支付宝收钱回调
     * @param orderId    支付订单主表id
     * @param request   返回数据
     */
    @PostMapping("callback/{orderId}")
    @HystrixCommand
    public void callabck(@PathVariable String orderId, HttpServletRequest request) {
        Map<String, String> parameterMap = this.getParameterMap(request);
        // 判断支付或者退费状态，成功时才进行处理
        String trade_status = parameterMap.get("trade_status");
        if ("TRADE_SUCCESS".equals(trade_status)){
            System.out.println("支付成功");
            try {
                // 验证是否为支付宝发送的请求
                boolean flag = AlipaySignature.rsaCheckV1(parameterMap, AlipayConfig.alipay_public_key, AlipayConfig.charset, AlipayConfig.sign_type);
                System.out.println("验证签名成功");
                // 判断是收费还是退费
                String refund_fee = parameterMap.get("refund_fee");
                if (StringUtils.isNotBlank(refund_fee)) {
                    // 不为空表示为退费
                    System.out.println("支付宝退费成功，退费的子订单Id："+ parameterMap.get("out_biz_no"));
                } else {
                    // 收费
                    System.out.println("支付宝收费成功，平台Id："+ parameterMap.get("trade_no"));
                    // 更新支付订单主表数据的状态||支付订单详情表数据状态||处方详情表数据状态
                    // 涉及三张表数据状态的修改
                    String trade_no = parameterMap.get("trade_no");
                    this.orderChargeService.paySuccess(orderId, trade_no);
                }
            } catch (AlipayApiException e) {
                // 如果不是支付宝的请求，就会抛出异常被catch捕获
                e.printStackTrace();
                System.out.println("验证签名出现异常");
            }
        } else if ("WAIT_BUYER_PAY".equals(trade_status)){
            System.out.println("交易创建，等待支付");
        }
    }

    /**
     * 获取request中的参数集合转Map
     * Map<String,String> parameterMap = RequestUtil.getParameterMap(request)
     * @param request
     * @return
     */
    public Map<String, String> getParameterMap(HttpServletRequest request) {
        Map<String, String> map = new HashMap<>();
        Enumeration<String> paramNames = request.getParameterNames();
        while (paramNames.hasMoreElements()) {
            String paramName = paramNames.nextElement();
            String[] paramValues = request.getParameterValues(paramName);
            if (paramValues.length == 1) {
                String paramValue = paramValues[0];
                if (paramValue.length() != 0) {
                    map.put(paramName, paramValue);
                }
            }
        }
        return map;
    }
}
