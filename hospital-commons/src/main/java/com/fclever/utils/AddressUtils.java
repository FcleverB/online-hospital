package com.fclever.utils;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 根据IP地址查询真实地址的工具类
 * @author Fclever
 * @create 2020-10-20 08:27
 */
public class AddressUtils {

    private static final Logger log = LoggerFactory.getLogger(AddressUtils.class);

    // 淘宝服务查询得劲接口
    public static final String IP_URL = "http://ip.taobao.com/service/getIpInfo.php";

    public static String getRealAddressByIP(String ip) {
        String address = "XX XX";
        // 内网不查询
        if (IpUtils.internalIp(ip)) {
            return "内网IP";
        }

        String rspStr = HttpUtils.sendPost(IP_URL, "ip=" + ip);
        if (StringUtils.isEmpty(rspStr)) {
            log.error("获取地理位置异常 {}", ip);
            return address;
        }
        // 转换为json对象
        JSONObject obj = JSONObject.parseObject(rspStr);
        // 获取数据
        JSONObject data = obj.getObject("data", JSONObject.class);
        // 区域
        String region = data.getString("region");
        // 城市
        String city = data.getString("city");
        address = region + " " + city;
        return address;
    }

}
