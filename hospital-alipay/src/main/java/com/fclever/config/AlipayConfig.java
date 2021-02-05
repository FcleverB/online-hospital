package com.fclever.config;

/**
 * 支付相关的常量
 * @author Fclever
 * @create 2021-02-05 14:13
 */
public class AlipayConfig {

    // 应用id
    public static String appid = "2021000117609654";

    // SHA256withRsa对应支付宝公钥
    public static String alipay_public_key = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAphoel4oSM9qkvBKpA1muMoAUrD3ryTvehMPQmE2MgL2Iv1oy+7Vm9SwjESsR8T5la1LJPT+Q7s0aiuxJATQhAzFODZHlsw/gljrjew+gzAttMW5GBG+Gy1nfZ2jiBRX4W2MSijgjy+XR906Pjxz5MPSz8P7PbfY4FVOSGh4MkmoXLldAMQA/t+qBc2zMPaM2xFYep3CJFfKChWCauYibdJYhIJ0u9H89qz8ym3mYZOQgREMWYJqtsDlzBKbDN5NiRXooY5KCpk6/5hk1TgcSLwAuQdWwsYVoOAN+2hsZHLCaHPQRmtWvoLQmuRMF2dPHqIwoDX4vGAj3girv0IhJxwIDAQAB";

    // 签名方式
    public static String sign_type = "RSA2";

    //  字符编码类型
    public static String charset = "utf-8";
}
