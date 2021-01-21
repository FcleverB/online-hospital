package com.fclever.utils;

import cn.hutool.core.date.DateUtil;

/**
 * 根据当前时间判断时间段工具类（上午、下午、晚上）
 * @author Fclever
 * @create 2021-01-21 09:50
 */
public class DateUtils {

    /**
     * 根据当前时间计算得出时间段-上午、下午、晚上
     *  这里的计算规则需要和前端的计算规则保持一致就可以
     * @return
     */
    public static String getCurrentTimeType() {
        int hour = DateUtil.hour(DateUtil.date(), true);
        if (hour >= 6 && hour < 12) {
            return "1";
        } else if (hour >= 12 && hour < 18) {
            return "2";
        } else {
            return "3";
        }
    }
}
