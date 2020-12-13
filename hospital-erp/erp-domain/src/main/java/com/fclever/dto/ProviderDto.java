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
import java.util.Date;

/**
@author Fclever
@create 2020-12-13 13:43
*/

/**
    * 供应商信息表
    */
@ApiModel(value="com-fclever-dto-ProviderDto")
@Data
@EqualsAndHashCode(callSuper=true)
@AllArgsConstructor
@NoArgsConstructor
public class ProviderDto extends BaseDto {
    /**
     * 供应商主键id
     */
    @ApiModelProperty(value="供应商主键id")
    private Long providerId;

    /**
     * 供应商名称
     */
    @NotBlank(message = "供应商名称不能为空")
    @ApiModelProperty(value="供应商名称")
    private String providerName;

    /**
     * 联系人姓名
     */
    @NotBlank(message = "联系人姓名不能为空")
    @ApiModelProperty(value="联系人姓名")
    private String contactName;

    /**
     * 联系人手机
     */
    @NotBlank(message = "联系人手机不能为空")
    @ApiModelProperty(value="联系人手机")
    private String contactTel;

    /**
     * 银行账号
     */
    @NotBlank(message = "银行账号不能为空")
    @ApiModelProperty(value="银行账号")
    private String bankAccount;

    /**
     * 供应商地址
     */
    @ApiModelProperty(value="供应商地址")
    private String providerAddress;

    /**
     * 状态（0正常 1停用）sys_normal_disable
     */
    @NotBlank(message = "状态不能为空")
    @ApiModelProperty(value="状态（0正常 1停用）sys_normal_disable")
    private String status;
}