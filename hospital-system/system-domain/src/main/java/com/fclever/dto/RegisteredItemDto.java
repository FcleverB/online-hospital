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
 * 挂号费用
 @author Fclever
 @create 2020-12-05 16:18
 */

/**
 * 挂号项目信息表
 */
@ApiModel(value="com-fclever-dto-RegisteredItemDto")
@Data
@EqualsAndHashCode(callSuper=true)
@AllArgsConstructor
@NoArgsConstructor
public class RegisteredItemDto extends BaseDto {
    /**
     * 挂号项目ID
     */
    @ApiModelProperty(value="挂号项目ID")
    private Long regItemId;

    /**
     * 挂号项目名称
     */
    @ApiModelProperty(value="挂号项目名称")
    @NotBlank(message = "挂号项目名称不能为空")
    private String regItemName;

    /**
     * 挂号费用
     */
    @ApiModelProperty(value="挂号费用")
    @NotNull(message = "挂号费用不能为空")
    private BigDecimal regItemFee;

    /**
     * 状态（0正常 1停用）
     */
    @ApiModelProperty(value="状态（0正常 1停用）")
    @NotBlank(message = "状态不能为空")
    private String status;
}