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

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
@author Fclever
@create 2020-10-20 13:13
*/

/**
    * 用户信息表
    */
@ApiModel(value="com-fclever-dto-UserDto")
@Data
@EqualsAndHashCode(callSuper=true)
@AllArgsConstructor
@NoArgsConstructor
public class UserDto extends BaseDto{
    /**
     * 用户ID
     */
    @ApiModelProperty(value="用户ID")
    private Long userId;

    /**
     * 部门ID
     */
    @ApiModelProperty(value="科室ID")
    @NotNull(message = "科室id不能为空")
    private Long deptId;

    /**
     * 用户账号
     */
    @ApiModelProperty(value="用户名称")
    @NotBlank(message = "用户名称不能为空")
    private String userName;

    /**
     * 用户类型（0超级用户为 1为系统用户）
     */
    @ApiModelProperty(value="用户类型（0超级用户为 1为系统用户）")
    private String userType;

    /**
     * 用户性别（0男 1女 2未知）
     */
    @ApiModelProperty(value="用户性别（0男 1女 2未知）")
    @NotBlank(message = "用户性别不能为空")
    private String sex;

    /**
     * 年龄
     */
    @ApiModelProperty(value="年龄")
    private Integer age;

    /**
     * 头像
     */
    @ApiModelProperty(value="头像")
    private String picture;

    /**
     * 学历 sys_dict_type:sys_user_background
     */
    @ApiModelProperty(value="学历 字典：sys_user_background")
    @NotBlank(message = "用户学历不能为空")
    private String background;

    /**
     * 电话
     */
    @ApiModelProperty(value="电话")
    @NotBlank(message = "电话不能为空")
    private String phone;

    /**
     * 用户邮箱
     */
    @ApiModelProperty(value="用户邮箱")
    private String email;

    /**
     * 擅长
     */
    @ApiModelProperty(value="爱好")
    private String hobby;

    /**
     * 荣誉
     */
    @ApiModelProperty(value="荣誉")
    private String honor;

    /**
     * 简介
     */
    @ApiModelProperty(value="简介")
    private String introduction;

    /**
     * 医生级别sys_dict_type:sys_user_level
     */
    @ApiModelProperty(value=",医生级别   字典：sys_user_level")
    @NotBlank(message = "医生级别不能为空")
    private String userRank;

    /**
     * 帐号状态（0正常 1停用）
     */
    @ApiModelProperty(value="帐号状态（0正常 1停用）")
    @NotBlank(message = "账号状态不能为空")
    private String status;

    /**
     * 是否需要参与排班0需要,1 不需要
     */
    @ApiModelProperty(value="是否需要参与排班0需要,1 不需要")
    @NotBlank(message = "是否排班信息不能为空")
    private String schedulingFlag;
}