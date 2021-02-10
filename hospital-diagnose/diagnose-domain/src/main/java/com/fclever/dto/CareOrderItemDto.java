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
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 处方详情数据传输类
 * @author Fclever
 * @create 2021-01-26 13:51
 */
@ApiModel(value="com-fclever-dto-CareOrderItemDto")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CareOrderItemDto implements Serializable {
    /**
     * 开诊明细ID
     */
    @ApiModelProperty(value = "处方项目ID")
    private String itemId;

    /**
     * 所属处方id
     */
    @ApiModelProperty(value = "所属处方id")
    private String coId;

    /**
     * 药品或者检查项目id
     */
    @ApiModelProperty(value = "药品或者检查项目id")
    @NotBlank(message = "药品或者检查项目id不能为空")
    private String itemRefId;

    /**
     * 药品或检查项目名称
     */
    @ApiModelProperty(value = "药品或检查项目名称")
    @NotBlank(message = "药品或者检查项目名称不能为空")
    private String itemName;

    /**
     * 项目类型0药品  还是1检查项
     */
    @ApiModelProperty(value = "项目类型0药品  还是1检查项")
    @NotBlank(message = "项目类型不能为空")
    private String itemType;

    /**
     * 数量
     */
    @ApiModelProperty(value = "数量")
    @NotNull(message = "数量不能为空")
    private BigDecimal num;

    /**
     * 单价
     */
    @ApiModelProperty(value = "单价")
    @NotNull(message = "单价不能为空")
    private BigDecimal price;

    /**
     * 金额
     */
    @ApiModelProperty(value = "金额")
    @NotNull(message = "金额不能为空")
    private BigDecimal amount;

    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String remark;

    /**
     * 状态，0未支付，1已支付，2，已退费  3，已完成 字典表his_order_details_status
     */
    @ApiModelProperty(value="状态，0未支付，1已支付，2，已退费  3，已完成 字典表his_order_details_status")
    private String status;

    /**
     * 支付类型 0 现金 1 支付宝 字典表	his_pay_type_status
     */
    @ApiModelProperty(value="支付类型 0 现金 1 支付宝 字典表\this_pay_type_status")
    private String payType;

    /**
     * 退费类型 0 现金 1 支付宝 字典表	his_pay_type_status
     */
    @ApiModelProperty(value="退费类型 0 现金 1 支付宝 字典表 his_pay_type_status")
    private String backType;
}
