package com.fclever.aspectj.enums;

/**
 * 业务类型枚举类
 *      增删改和其他
 *      这些类型数据必须和数据库的对应码表的dict_value值一致，因为前端进行码表转换的时候，是根据保存的操作类型值与字典类型表的数据进行匹配，
 *              而且新增的操作日志的操作类型code值就是对应@Log注解选择的枚举的序数
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
