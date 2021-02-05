package com.fclever.pay;

import java.util.Map;

/**
 * @author Fclever
 * @create 2021-02-04 22:49
 */
public class TestPay {

    public static void main(String[] args) {
        // (必填) 订单标题，粗略描述用户的支付目的。如“喜士多（浦东店）消费”
        String subject = "古茗（师大店）消费";
        // (必填) 订单总金额，单位为元，不能超过1亿元
        String totalAmount = "20";
        // (必填) 商户网站订单系统中唯一订单号，64个字符以内，只能包含字母、数字、下划线，需保证商户系统端不能重复------对应支付订单信息表中的orderId即可
        // ！！！！每个订单号对应生成一个二维码，如果订单号一样，多次运行程序产生的二维码只能用一次
        String outTradeNo = "ORDER19981122";
        // (可选) 订单不可打折金额，可以配合商家平台配置折扣活动，如果酒水不参与打折，则将对应金额填写至此字段，如果该值未传入,但传入了【订单总金额】,【打折金额】,则该值默认为【订单总金额】-【打折金额】
        String undiscountableAmount = null;
        // 订单描述，可以对交易或商品进行一个详细地描述，比如填写"购买商品2件共15.00元"
        String body = "购买奶茶2杯共20.00元";
        // 支付宝服务器主动通知商户服务器里指定的页面http路径,根据需要设置
        String notifyUrl = "http://j29i754289.51vip.biz/pay/callback/" + outTradeNo;
        Map<String, Object> result = PayService.pay(subject, totalAmount, outTradeNo, undiscountableAmount, body, notifyUrl);
        System.out.println(result);
    }
}
