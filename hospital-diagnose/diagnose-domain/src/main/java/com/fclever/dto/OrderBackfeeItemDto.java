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

/**
@author Fclever
@create 2021-02-09 09:14
*/

/**
    * 1退费订单信息详情表
    */
@ApiModel(value="com-fclever-dto-OrderBackfeeItemDto")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderBackfeeItemDto implements Serializable {

    /**
     * 详情ID和his_care_order_*表里面的ID一样
     */
    @ApiModelProperty(value="详情ID和his_care_order_*表里面的ID一样")
    @NotBlank(message = "详情Id不能为空")
    private String itemId;

    /**
     * 处方ID【备用】
     */
    @ApiModelProperty(value="处方ID【备用】")
    @NotBlank(message = "处方ID不能为空")
    private String coId;

    /**
     * 项目名称
     */
    @ApiModelProperty(value="项目名称")
    @NotBlank(message = "项目名称不能为空")
    private String itemName;

    /**
     * 项目价格
     */
    @ApiModelProperty(value="项目价格")
    @NotNull(message = "项目价格不能为空")
    private BigDecimal itemPrice;

    /**
     * 项目数量
     */
    @ApiModelProperty(value="项目数量")
    @NotBlank(message = "项目数量不能为空")
    private Integer itemNum;

    /**
     * 小计
     */
    @ApiModelProperty(value="小计")
    @NotBlank(message = "小计不能为空")
    private Long itemAmount;

    /**
     * 订单ID his_oder_backfee主键
     */
    @ApiModelProperty(value="订单ID his_oder_backfee主键")
    private String backId;

    /**
     * 项目类型0药品  还是1检查项
     */
    @ApiModelProperty(value="项目类型0药品  还是1检查项")
    private String itemType;

    /**
     * 状态，0未支付，1已支付，2，已退费  3，已完成   字典表 his_order_details_status
     */
    @ApiModelProperty(value="状态，0未支付，1已支付，2，已退费  3，已完成   字典表 his_order_details_status")
    private String status;
}