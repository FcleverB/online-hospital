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
    private String regItemName;

    /**
     * 挂号费用
     */
    @ApiModelProperty(value="挂号费用")
    private BigDecimal regItemFee;

    /**
     * 状态（0正常 1停用）
     */
    @TableField(value = "status")
    @ApiModelProperty(value="状态（0正常 1停用）")
    private String status;

    /**
     * 删除标志（0正常 1删除）
     */
    @TableField(value = "del_flag")
    @ApiModelProperty(value="删除标志（0正常 1删除）")
    private String delFlag;
}