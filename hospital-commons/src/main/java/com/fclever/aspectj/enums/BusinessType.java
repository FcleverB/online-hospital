package com.fclever.aspectj.enums;

/**
 * 业务类型枚举类
 *      增删改和其他
 *      这些类型数据必须和数据库的对应码表的dict_value值一致
 *      ordinal序数从0开始
 * @author Fclever
 * @create 2020-11-16 12:28
 */
public enum BusinessType {

    /**
     * 其它
     */
    OTHER,

    /**
     * 新增
     */
    INSERT,

    /**
     * 修改
     */
    UPDATE,

    /**
     * 删除
     */
    DELETE,

    /**
     * 同步字典数据到缓存
     */
    CACHEASYNC
}
