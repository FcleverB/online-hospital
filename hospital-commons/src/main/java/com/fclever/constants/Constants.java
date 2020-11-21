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
     * 有效状态
     */
    public static final String STATUS_TRUE = "0";
    public static final String STATUS_FALSE = "1";

    /**
     * 菜单类型 M一级菜单 C二级菜单 F权限
     */
    public static final String MENU_TYPE_M = "M";
    public static final String MENU_TYPE_C = "C";
    public static final String MENU_TYPE_F = "F";

    /**
     * redis的字典前缀
     */
    public static final String DICT_REDIS_PROFIX="dict:";

    /**
     * 登录状态  0 成功  1失败
     */
    public static final String LOGIN_SUCCESS = "0";
    public static final String LOGIN_ERROR = "1";
    /**
     * 登陆类型0系统用户1患者用户
     */
    public static final String LOGIN_TYPE_SYSTEM = "0" ;
    public static final String LOGIN_TYPE_PATIENT = "1" ;
}
