package com.fclever.aspectj.annotation;

import com.fclever.aspectj.enums.BusinessType;
import com.fclever.aspectj.enums.OperatorType;

import java.lang.annotation.*;

/**
 * 自定义操作日志注解
 *      修饰注解的注解---元注解
 *      @Target 声明该注解的作用范围，这里只能作用在参数和方法上
 *      @Retention  声明该注解不仅被保存到class文件中，jvm加载class文件之后，仍然存在
 *      @Documented 声明该注解会被javadoc工具记录
 * @author Fclever
 * @create 2020-11-16 12:23
 */
@Target({ ElementType.PARAMETER,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Log {

    /**
     * 模块
     *      默认为“”
     */
    public String title() default "";

    /**
     * 业务类型  增删改  其他
     *      默认为其他
     */
    public BusinessType businessType() default BusinessType.OTHER;

    /**
     * 操作人类别
     *      默认为后台用户
     */
    public OperatorType operatorType() default OperatorType.MANAGE;

    /**
     * 是否保存请求的参数
     *      默认保存
     */
    public boolean isSaveRequestData() default true;
}
