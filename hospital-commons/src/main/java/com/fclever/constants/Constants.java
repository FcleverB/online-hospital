package com.fclever.constants;

/**
 * 系统要使用的常量数据
 * @author Fclever
 * @create 2020-10-19 08:56
 */
public class Constants {

    /**
     * 令牌
     */
    public static final String TOKEN = "token";
    /**
     * 系统用户类型
     */
    public static final String USER_ADMIN = "0";
    public static final String USER_NORMAL = "1";
    /**
     * 返回检验码
     */
    public static final String UNIQUE = "0";
    public static final String NOT_UNIQUE = "1";
    /**
     * 有效状态
     */
    public static final String STATUS_TRUE = "0";
    public static final String STATUS_FALSE = "1";

    /**
     * 删除状态
     */
    public static final String DEL_FALSE = "0";
    public static final String DEL_TRUE = "1";
    /**
     * 菜单类型 M一级菜单 C二级菜单 F权限
     */
    public static final String MENU_TYPE_M = "M";
    public static final String MENU_TYPE_C = "C";
    public static final String MENU_TYPE_F = "F";
    /**
     * 入库单状态 1未提交2待审核 3审核通过 4审核失败 5作废 6 入库成功
     */
    public static final String STOCK_PURCHASE_STATUS_1 = "1";
    public static final String STOCK_PURCHASE_STATUS_2 = "2";
    public static final String STOCK_PURCHASE_STATUS_3 = "3";
    public static final String STOCK_PURCHASE_STATUS_4 = "4";
    public static final String STOCK_PURCHASE_STATUS_5 = "5";
    public static final String STOCK_PURCHASE_STATUS_6 = "6";
    /**
     * 入库状态 0未入库 1已入库
     */
    public static final String STOCK_STORAGE_0 = "0";
    public static final String STOCK_STORAGE_1 = "1";
    /**
     * 默认预警值
     */
    Long DEFAULT_WARNING = 50L;

    /**
     * 排班状态
     */
    public static final String SCHEDULING_FLAG_TRUE = "0";
    public static final String SCHEDULING_FLAG_FALSE = "1";
    /**
     * 是否完善信息
     */
    public static final String IS_FINAL_FALSE = "0";
    public static final String IS_FINAL_TRUE = "1";
    /**
     * 挂号单状态
     */
    public static final String REG_STATUS_0 = "0"; //待支付
    public static final String REG_STATUS_1 = "1"; //待就诊
    public static final String REG_STATUS_2 = "2"; //就诊中
    public static final String REG_STATUS_3 = "3"; //就诊完成
    public static final String REG_STATUS_4 = "4"; //已退号
    public static final String REG_STATUS_5 = "5"; //已作废
    /**
     * 处方类型
     */
    public static final String CO_TYPE_MEDICINES = "0";
    public static final String CO_TYPE_CHECK = "1";
    /**
     * 支付单状态状态，0未支付,1已支付，2支付超时
     */
    public static final String ORDER_STATUS_0 = "0";
    public static final String ORDER_STATUS_1 = "1";
    public static final String ORDER_STATUS_2 = "2";
    /**
     * 订单子项目支付状态
     * 0未支付，1已支付，2，已退费  3，已完成
     */
    public static final String ORDER_DETAILS_STATUS_0 = "0";
    public static final String ORDER_DETAILS_STATUS_1 = "1";
    public static final String ORDER_DETAILS_STATUS_2 = "2";
    public static final String ORDER_DETAILS_STATUS_3 = "3";
    /**
     * 检查状态  0 检查中   1检查完成
     */
    public static final String RESULT_STATUS_0 = "0";
    public static final String RESULT_STATUS_1 = "1";

    /**
     * 退费单状态，订单状态0未退费  1 退费成功 2退费失败
     */
    public static final String ORDER_BACKFEE_STATUS_0 = "0";
    public static final String ORDER_BACKFEE_STATUS_1 = "1";
    public static final String ORDER_BACKFEE_STATUS_2 = "2";
    /**
     * 支付类型
     */
    public static final String PAY_TYPE_0 = "0";//现金
    public static final String PAY_TYPE_1 = "1";//支付宝

    /**
     * redis的字典前缀
     */
    public static final String DICT_REDIS_PROFIX="dict:";

    /**
     * 登陆状态  0 成功  1失败
     */
    public static final String LOGIN_SUCCESS = "0";
    public static final String LOGIN_ERROR = "1";
    /**
     * 登陆类型0系统用户1患者用户
     */
    public static final String LOGIN_TYPE_SYSTEM = "0" ;
    public static final String LOGIN_TYPE_PATIENT = "1" ;
}
