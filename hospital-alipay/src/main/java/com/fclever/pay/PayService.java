package com.fclever.pay;

import com.alipay.api.response.AlipayTradePrecreateResponse;
import com.alipay.demo.trade.config.Configs;
import com.alipay.demo.trade.model.builder.AlipayTradePrecreateRequestBuilder;
import com.alipay.demo.trade.model.result.AlipayF2FPrecreateResult;
import com.alipay.demo.trade.service.AlipayTradeService;
import com.alipay.demo.trade.service.impl.AlipayTradeServiceImpl;
import com.alipay.demo.trade.utils.ZxingUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * 支付宝当面付测试
 * @author Fclever
 * @create 2021-02-04 22:13
 */
public class PayService {

    // 输出日志
    static Log log = LogFactory.getLog("PayService");
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
     * 进行支付操作的方法
     * @param subject   (必填) 订单标题，粗略描述用户的支付目的。如“喜士多（浦东店）消费”
     * @param totalAmount   (必填) 订单总金额，单位为元，不能超过1亿元
     * @param outTradeNo    (必填) 商户网站订单系统中唯一订单号，64个字符以内，只能包含字母、数字、下划线，需保证商户系统端不能重复------对应支付订单信息表中的orderId即可
     * @param undiscountableAmount  (可选) 订单不可打折金额，可以配合商家平台配置折扣活动，如果酒水不参与打折，则将对应金额填写至此字段，如果该值未传入,但传入了【订单总金额】,【打折金额】,则该值默认为【订单总金额】-【打折金额】
     * @param body  订单描述，可以对交易或商品进行一个详细地描述，比如填写"购买商品2件共15.00元"
     * @param notifyUrl 支付宝服务器主动通知商户服务器里指定的页面http路径,根据需要设置
     * @return  返回结果
     */
    public static Map<String, Object> pay(String subject, String totalAmount, String outTradeNo,String undiscountableAmount,String body, String notifyUrl){
        AlipayTradePrecreateRequestBuilder builder =new AlipayTradePrecreateRequestBuilder()
                .setSubject(subject)
                .setTotalAmount(totalAmount)
                .setOutTradeNo(outTradeNo)
                .setUndiscountableAmount(undiscountableAmount)
                .setBody(body)
                .setOperatorId(operatorId)
                .setStoreId(storeId)
                .setTimeoutExpress(timeoutExpress)
                .setNotifyUrl(notifyUrl);
        // 发起支付操作
        AlipayF2FPrecreateResult result = tradeService.tradePrecreate(builder);
        // 定义返回值
        Map<String, Object> map = new HashMap<>();
        // 定义状态码
        Integer code;
        // 定义消息
        String msg;
        switch (result.getTradeStatus()) {
            case SUCCESS:
                log.info("支付宝预下单成功: )");
                AlipayTradePrecreateResponse res = result.getResponse();
                // 得到支付码，本质就是一个URL
                String qrCode = res.getQrCode();
                map.put("qrCode", qrCode);
                code = 200;
                msg = "支付宝支付成功";
                break;
            case FAILED:
                log.error("支付宝预下单失败!!!");
                code = 500;
                msg = "支付宝支付失败";
                break;
            case UNKNOWN:
                log.error("系统异常，预下单状态未知!!!");
                code = 404;
                msg = "系统异常，预下单状态未知!!!";
                break;
            default:
                log.error("不支持的交易状态，交易返回异常!!!");
                code = 404;
                msg = "不支持的交易状态，交易返回异常!!!";
                break;
        }
        map.put("code", code);
        map.put("msg", msg);
        return map;
    }
}
