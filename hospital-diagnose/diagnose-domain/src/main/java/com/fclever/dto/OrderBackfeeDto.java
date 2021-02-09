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
import java.math.BigDecimal;
import java.util.Date;

/**
@author Fclever
@create 2021-02-09 09:13
*/

/**
    * 1退费订单信息表
    */
@ApiModel(value="com-fclever-dto-OrderBackfeeDto")
@Data
@EqualsAndHashCode(callSuper=true)
@AllArgsConstructor
@NoArgsConstructor
public class OrderBackfeeDto extends BaseDto {

    /**
     * 退费ID
     */
    @ApiModelProperty(value="退费ID")
    private String backId;

    /**
     * 总费用
     */
    @ApiModelProperty(value="退费费用")
    @NotNull(message = "退费费用不能为空")
    private BigDecimal backAmount;

    /**
     * 病历ID
     */
    @ApiModelProperty(value="病历ID")
    @NotBlank(message = "病历Id不能为空")
    private String chId;

    /**
     * 挂号单
     */
    @ApiModelProperty(value="挂号单据id")
    @NotBlank(message = "挂号单据id不能为空")
    private String registrationId;

    /**
     * 患者名称
     */
    @ApiModelProperty(value="患者名称")
    @NotBlank(message = "患者名称不能为空")
    private String patientName;

    /**
     * 订单状态0未退费  1 退费成功 2退费失败  字典表his_backfee_status
     */
    @ApiModelProperty(value="订单状态0未退费  1 退费成功 2退费失败  字典表his_backfee_status")
    private String backStatus;

    /**
     * 退费类型0现金1支付宝  字典表his_pay_type_status
     */
    @ApiModelProperty(value="退费类型0现金1支付宝  字典表his_pay_type_status")
    private String backType;

    /**
     * 关联订单ID his_order_charge  
     */
    @ApiModelProperty(value="关联订单ID his_order_charge  ")
    private String orderId;

    /**
     * 退费交易ID
     */
    @ApiModelProperty(value="退费交易ID")
    private String backPlatformId;

    /**
     * 退费交易时间
     */
    @ApiModelProperty(value="退费交易时间")
    private Date backTime;
}