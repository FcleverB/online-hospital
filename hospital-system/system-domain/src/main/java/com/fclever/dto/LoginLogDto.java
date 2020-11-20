package com.fclever.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fclever.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
@author Fclever
@create 2020-11-19 08:29
*/

/**
    * 登录日志表
 *      与前端进行交互，封装查询条件
    */
@ApiModel(value="com-fclever-dto-LoginLogDto")
@Data
@EqualsAndHashCode(callSuper=true)
@AllArgsConstructor
@NoArgsConstructor
public class LoginLogDto extends BaseDto {

    /**
     * 用户名称
     */
    @ApiModelProperty(value="用户名称")
    private String userName;

    /**
     * 登陆账号
     */
    @ApiModelProperty(value="登陆账号")
    private String loginAccount;

    /**
     * 登录IP地址
     */
    @ApiModelProperty(value="登录IP地址")
    private String ipAddr;

    /**
     * 登录状态（0成功 1失败）字典表
     */
    @ApiModelProperty(value="登录状态（0成功 1失败）字典表")
    private String loginStatus;

    /**
     * 登陆类型0系统用户1患者用户 字典表
     */
    @ApiModelProperty(value="登陆类型0系统用户1患者用户 字典表")
    private String loginType;

    /**
     * 访问时间
     */
    @ApiModelProperty(value="访问时间")
    private Date loginTime;
}