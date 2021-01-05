package com.fclever.constants;

/**
 * 系统要使用的常量数据
 * @author Fclever
 * @create 2020-10-19 08:56
 */
// 接口中默认变量存在public static final
public interface Constants {

    /**
     * 令牌
     */
    String TOKEN = "token";

    /**
     * 系统用户类型
     */
    String USER_ADMIN = "0";
    String USER_NORMAL = "1";

    /**
     * 有效状态
     */
    String STATUS_TRUE = "0";
    String STATUS_FALSE = "1";

    /**
     * 菜单类型 M一级菜单 C二级菜单 F权限
     */
    String MENU_TYPE_M = "M";
    String MENU_TYPE_C = "C";
    String MENU_TYPE_F = "F";

    /**
     * redis的字典前缀
     */
    String DICT_REDIS_PROFIX="dict:";

    /**
     * 登录状态  0 成功  1失败
     */
    String LOGIN_SUCCESS = "0";
    String LOGIN_ERROR = "1";

    /**
     * 登陆类型0系统用户1患者用户
     */
    String LOGIN_TYPE_SYSTEM = "0" ;
    String LOGIN_TYPE_PATIENT = "1" ;

    /**
     * 入库单状态：1未提交   2待审核    3审核通过   4审核不通过  5作废 6入库成功
     */
    String STOCK_PURCHASE_STATUS_1 = "1";
    String STOCK_PURCHASE_STATUS_2 = "2";
    String STOCK_PURCHASE_STATUS_3 = "3";
    String STOCK_PURCHASE_STATUS_4 = "4";
    String STOCK_PURCHASE_STATUS_5 = "5";
    String STOCK_PURCHASE_STATUS_6 = "6";

    /**
     * 生产入库单据id的前缀
     */
    String ID_PROFIX_CG = "CG";

    /**
     * 是否需要排班标志
     */
    String SCHEDULING_FLAG_FALSE = "0";
    String SCHEDULING_FLAG_TRUE = "1";
}
