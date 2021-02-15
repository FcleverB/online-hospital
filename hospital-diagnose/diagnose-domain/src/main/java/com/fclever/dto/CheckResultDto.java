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
import java.util.List;

/**
@author Fclever
@create 2021-02-15 13:11
*/

/**
    * 处方检查结果信息表
    */
@ApiModel(value="com-fclever-dto-CheckResultDto")
@Data
@EqualsAndHashCode(callSuper=true)
@AllArgsConstructor
@NoArgsConstructor
public class CheckResultDto extends BaseDto {

    /**
     * 检查项目ID
     */
    @ApiModelProperty(value="检查项目IDS")
    private List<Integer> checkItemIds;

    /**
     * 患者姓名
     */
    @ApiModelProperty(value="患者姓名")
    private String patientName;

    /**
     * 关联的挂号单据Id
     */
    @ApiModelProperty(value="关联的挂号单据Id")
    private String registrationId;

    /**
     * 是否录入检查结果0 检测中  1 检测完成  字典表 his_check_result_status
     */
    @ApiModelProperty(value="是否录入检查结果0 检测中  1 检测完成  字典表 his_check_result_status")
    private String resultStatus;
}