package com.fclever.refund;

import com.alipay.demo.trade.config.Configs;
import com.alipay.demo.trade.model.builder.AlipayTradeRefundRequestBuilder;
import com.alipay.demo.trade.model.result.AlipayF2FRefundResult;
import com.alipay.demo.trade.service.AlipayTradeService;
import com.alipay.demo.trade.service.impl.AlipayTradeServiceImpl;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 支付宝退费
 * @author Fclever
 * @create 2021-02-05 14:57
 */
public class RefundService {

    // 输出日志
    static Log log = LogFactory.getLog("TestRefund");

    // 支付宝当面付的对象
    static AlipayTradeService tradeService;

    // 初始化对象
    static {
        // 一定要在创建AlipayTradeService之前设置参数-----这里是加载配置文件
        Configs.init("zfbinfo.properties");
        // 实例化对象
        tradeService = new AlipayTradeServiceImpl.ClientBuilder().build();
    }

    // 商户操作员编号，添加此参数可以为商户操作员做销售统计
    static String operatorId = "test_operator_id";

    // (必填) 商户门店编号，通过门店号和商家后台可以配置精准到门店的折扣信息，详询支付宝技术支持
    static String storeId = "test_store_id";

    // 支付超时，定义为120分钟
    static String timeoutExpress = "120m";

    /**
     *  退费操作
     * @param tradeNo 支付成功之后返回的内容【属于支付宝平台的id】
     * @param outTradeNo    外部订单号，需要退款交易的商户外部订单号
     * @param refundAmount  退款金额，该金额必须小于等于订单的支付金额，单位为元
     * @param outRequestNo  商户退款请求号，相同支付宝交易号下的不同退款请求号对应同一笔交易的不同退款申请，对于相同支付宝交易号下多笔相同商户退款请求号的退款交易，支付宝只会进行一次退款
     * @param refundReason  退款原因，可以说明用户退款原因，方便为商家后台提供统计
     */
    public static void payRefund(String tradeNo,String outTradeNo,String refundAmount,String outRequestNo,String refundReason) {
        AlipayTradeRefundRequestBuilder builder = new AlipayTradeRefundRequestBuilder()
                .setOutTradeNo(outTradeNo)
                .setTradeNo(tradeNo)
                .setRefundAmount(refundAmount)
                .setRefundReason(refundReason)
                .setOutRequestNo(outRequestNo)
//                .setNotifyUrl(notifyUrl) // 没有用
                .setStoreId(storeId);
        // 执行退款方法
        AlipayF2FRefundResult result = tradeService.tradeRefund(builder);
        switch (result.getTradeStatus()) {
            case SUCCESS:
                log.info("支付宝退款成功: )");
                break;
            case FAILED:
                log.error("支付宝退款失败!!!");
                break;
            case UNKNOWN:
                log.error("系统异常，订单退款状态未知!!!");
                break;
            default:
                log.error("不支持的交易状态，交易返回异常!!!");
                break;
        }
    }
}
