package com.fclever.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fclever.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author Fclever
 * @create 2020-12-20 11:57
 */
@ApiModel(value="com-fclever-dto-PurchaseItemDto")
@Data
@EqualsAndHashCode()
@AllArgsConstructor
@NoArgsConstructor
// 这里没有继承BaseDto，因为该对象不需要分页处理以及创建时间啥的参数
/// 只需要序列表进行传输即可
public class PurchaseItemDto implements Serializable {
    /**
     * 详情ID
     */
    @ApiModelProperty(value="详情ID")
    private String itemId;

    /**
     * 采购单据ID
     */
    @NotBlank(message = "采购单据id不能为空")
    @ApiModelProperty(value="采购单据ID")
    private String purchaseId;

    /**
     * 药品ID
     */
    @NotBlank(message = "药品id不能为空")
    @ApiModelProperty(value="药品ID")
    private String medicinesId;

    /**
     * 采购数量
     */
    @NotNull(message = "采购数量不能为空")
    @ApiModelProperty(value="采购数量")
    private Integer purchaseNumber;

    /**
     * 批发价
     */
    @NotNull(message = "批发价不能为空")
    @ApiModelProperty(value="批发价")
    private BigDecimal tradePrice;

    /**
     * 批发额
     */
    @NotNull(message = "批发额不能为空")
    @ApiModelProperty(value="批发额")
    private BigDecimal tradeTotalAmount;

    /**
     * 药品生产批次号
     */
    @ApiModelProperty(value="药品生产批次号")
    private String batchNumber;

    /**
     * 备注
     */
    @ApiModelProperty(value="备注")
    private String remark;

    /**
     * 药品名称
     */
    @NotBlank(message = "药品名称不能为空")
    @ApiModelProperty(value="药品名称")
    private String medicinesName;

    /**
     * 药品分类 sys_dict_data表his_medicines_type
     */
    @NotBlank(message = "药品分类不能为空")
    @ApiModelProperty(value="药品分类 sys_dict_data表his_medicines_type")
    private String medicinesType;

    /**
     * 处方类型 sys_dict_data表his_prescription_type
     */
    @NotBlank(message = "药品分类不能为空")
    @ApiModelProperty(value="处方类型 sys_dict_data表his_prescription_type")
    private String prescriptionType;

    /**
     * 生产厂家ID
     */
    @NotBlank(message = "生产厂家不能为空")
    @ApiModelProperty(value="生产厂家ID")
    private String producterId;

    /**
     * 换算量
     */
    @NotNull(message = "换算量不能为空")
    @ApiModelProperty(value="换算量")
    private Integer conversion;

    /**
     * 单位（g/条）
     */
    @NotBlank(message = "单位不能为空")
    @ApiModelProperty(value="单位（g/条）")
    private String unit;

    /**
     * 关键字
     */
    @NotBlank(message = "关键字不能为空")
    @ApiModelProperty(value="关键字")
    private String keywords;
}
