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
     * 主键前缀
     */
    String ID_PREFIX_CG = "CG"; // 生产入库单据id的前缀
    String ID_PREFIX_HZ = "HZ"; // 患者信息id的前缀
    String ID_PREFIX_GH = "GH"; // 挂号主键id的前缀
    String ID_PREFIX_CH = "CH"; // 病历主键id的前缀
    String ID_PROFIX_CO = "CO"; // 处方id前缀
    String ID_PROFIX_ITEM = "ITEM"; // 处方项目id前缀
    String ID_PROFIX_ORDER = "ORDER"; // 支付订单id前缀

    /**
     * 是否需要排班标志
     */
    String SCHEDULING_FLAG_FALSE = "0";
    String SCHEDULING_FLAG_TRUE = "1";

    /**
     * 挂号单状态
     */
    String REG_STATUS_0 = "0"; // 待支付
    String REG_STATUS_1 = "1"; // 待就诊
    String REG_STATUS_2 = "2"; // 就诊中
    String REG_STATUS_3 = "3"; // 就诊完成
    String REG_STATUS_4 = "4"; // 已退号
    String REG_STATUS_5 = "5"; // 已作废

    /**
     * 患者信息是否完善
     */
    String IS_FINAL_FALSE = "0";
    String IS_FINAL_TRUE = "1";

    /**
     * 支付订单状态
     * 0 未支付 1已支付 2支付超时
     */
    String ORDER_STATUS_0 = "0";
    String ORDER_STATUS_1 = "1";
    String ORDER_STATUS_2 = "2";

    /**
     * 处方详情状态+支付订单详情状态
     * 0未支付，1已支付，2，已退费  3，已完成
     * his_order_details_status
     */
    String ORDER_DETAILS_STATUS_0 = "0";
    String ORDER_DETAILS_STATUS_1 = "1";
    String ORDER_DETAILS_STATUS_2 = "2";
    String ORDER_DETAILS_STATUS_3 = "3";

    /**
     * 支付订单状态
     * 0 现金  1 支付宝
     * his_pay_type_status
     */
    String PAY_TYPE_STATUS_0 = "0";
    String PAY_TYPE_STATUS_1 = "1";
}
