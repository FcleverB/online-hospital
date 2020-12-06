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
@create 2020-12-05 12:36
*/

/**
    * 检查项目表
    */
@ApiModel(value="com-fclever-dto-CheckItemDto")
@Data
@EqualsAndHashCode(callSuper=true)
@AllArgsConstructor
@NoArgsConstructor
public class CheckItemDto extends BaseDto {
    /**
     * 检查项目ID
     */
    @ApiModelProperty(value="检查项目ID")
    private Long checkItemId;

    /**
     * 项目名称
     */
    @ApiModelProperty(value="项目名称")
    @NotBlank(message = "项目名称不能为空")
    private String checkItemName;

    /**
     * 关键字【查询用】
     */
    @ApiModelProperty(value="关键字【查询用】")
    @NotBlank(message = "关键字不能为空")
    private String keywords;

    /**
     * 项目单价
     */
    @ApiModelProperty(value="项目单价")
    @NotNull(message = "项目单价不能为空")
    private BigDecimal unitPrice;

    /**
     * 项目成本
     */
    @ApiModelProperty(value="项目成本")
    @NotNull(message = "项目成本不能为空")
    private BigDecimal cost;

    /**
     * 计量单位
     */
    @ApiModelProperty(value="计量单位")
    @NotBlank(message = "计量单位不能为空")
    private String unit;

    /**
     * 项目类别IDsxt_sys_dict_type
     */
    @ApiModelProperty(value="项目类别IDsxt_sys_dict_type")
    @NotBlank(message = "项目类别不能为空")
    private String typeId;

    /**
     * 状态0正常1停用 sxt_sys_dict_type
     */
    @ApiModelProperty(value="状态0正常1停用 sxt_sys_dict_type")
    @NotBlank(message = "状态不能为空")
    private String status;
}