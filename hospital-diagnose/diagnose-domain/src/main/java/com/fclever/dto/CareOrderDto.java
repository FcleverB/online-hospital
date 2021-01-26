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
 * 处方数据传输类
 * @author Fclever
 * @create 2021-01-26 13:47
 */
@ApiModel(value="com-fclever-dto-CareOrderDto")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CareOrderDto extends BaseDto {
    /**
     * 处方ID
     */
    @ApiModelProperty(value = "处方ID")
    private String coId;

    /**
     * 处方类型0药用处方1检查处方
     */
    @ApiModelProperty(value = "处方类型0药用处方1检查处方")
    @NotBlank(message = "处方类型不能为空")
    private String coType;

    /**
     * 医生id
     */
    @ApiModelProperty(value = "医生id")
    private Long userId;

    /**
     * 患者id
     */
    @ApiModelProperty(value = "患者id")
    private String patientId;

    /**
     * 患者姓名
     */
    @ApiModelProperty(value = "患者姓名")
    private String patientName;

    /**
     * 关联病历id
     */
    @ApiModelProperty(value = "关联病历id")
    @NotBlank(message = "关联病历id不能为空")
    private String chId;

    /**
     * 处方全额
     */
    @ApiModelProperty(value = "处方全额")
    @NotNull(message = "处方全额不能为空")
    private BigDecimal allAmount;
}
