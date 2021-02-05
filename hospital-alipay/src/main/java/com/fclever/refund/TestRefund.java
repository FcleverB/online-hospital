package com.fclever.refund;


/**
 * 测试支付宝退费
 * @author Fclever
 * @create 2021-02-05 14:57
 */
public class TestRefund {

    public static void main(String[] args) {
        // 支付成功之后返回的内容【属于支付宝平台的id】---可以从收费返回数据中获得
        String tradeNo = "2021020522001428510501232038";
        // 外部订单号，需要退款交易的商户外部订单号---可以从收费返回数据中获得
        String outTradeNo = "ORDER19981122";
        // 退款金额，该金额必须小于等于订单的支付金额，单位为元
        String refundAmount = "10";
        // 商户退款请求号，相同支付宝交易号下的不同退款请求号对应同一笔交易的不同退款申请，对于相同支付宝交易号下多笔相同商户退款请求号的退款交易，支付宝只会进行一次退款
        String outRequestNo = "BK232423423";
        // 退款原因，可以说明用户退款原因，方便为商家后台提供统计
        String refundReason = "商品破损";
        // 支付宝服务器主动通知商户服务器里指定的页面http路径,根据需要设置
        // 退费的回调url是使用对应账单收费时回调的url，单独对退费设置没有用
//        String notifyUrl = "http://j29i754289.51vip.biz/pay/callback2/" + outTradeNo;
        // 执行退款操作
//        RefundService.payRefund(tradeNo, outTradeNo, refundAmount, outRequestNo, refundReason, notifyUrl);
        RefundService.payRefund(tradeNo, outTradeNo, refundAmount, outRequestNo, refundReason);
    }
}
