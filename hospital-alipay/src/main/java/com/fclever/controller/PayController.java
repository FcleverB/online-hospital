package com.fclever.controller;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.fclever.config.AlipayConfig;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
public class PayController {

    // 输出日志
    static Log log = LogFactory.getLog("PayController");

    /**
     * 支付宝收钱回调
     * @param orderId
     * @param request
     */
    @PostMapping("callback/{orderId}")
    public void callabck(@PathVariable String orderId, HttpServletRequest request) {
        System.out.println("收费");
        Map<String, String> parameterMap = this.getParameterMap(request);
        try {
            // 验证是否为支付宝发送的请求
            boolean flag = AlipaySignature.rsaCheckV1(parameterMap, AlipayConfig.alipay_public_key, AlipayConfig.charset, AlipayConfig.sign_type);
            if (flag) {
                // 是支付宝请求
                System.out.println("确认为支付宝发起的请求");
                System.out.println(parameterMap);
            } else {
                // 非支付宝请求
                System.out.println("非支付宝发起的请求");
            }
        } catch (AlipayApiException e) {
            e.printStackTrace();
            System.out.println("验证签名出现异常");
        }
    }

    /**
     * 支付宝退钱回调-------无意义，退费使用对应订单收费的回调地址
     * @param orderId
     * @param request
     */
    @PostMapping("callback2/{orderId}")
    public void callabck2(@PathVariable String orderId, HttpServletRequest request) {
        System.out.println("退费");
        Map<String, String> parameterMap = this.getParameterMap(request);
        try {
            // 验证是否为支付宝发送的请求
            boolean flag = AlipaySignature.rsaCheckV1(parameterMap, AlipayConfig.alipay_public_key, AlipayConfig.charset, AlipayConfig.sign_type);
            if (flag) {
                // 是支付宝请求
                System.out.println("确认为支付宝发起的请求");
                System.out.println(parameterMap);
            } else {
                // 非支付宝请求
                System.out.println("非支付宝发起的请求");
            }
        } catch (AlipayApiException e) {
            e.printStackTrace();
            System.out.println("验证签名出现异常");
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
