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

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
@author Fclever
@create 2020-11-21 20:47
*/

/**
    * 科室信息表
 *          封装查询条件
 *          如果有新增或者更新操作，那么需要根据具体需求来决定要保留更多字段
 *          并且根据需求来设置字段后端校验
    */
@ApiModel(value="com-fclever-dto-DeptDto")
@Data
@EqualsAndHashCode(callSuper=true)
@AllArgsConstructor
@NoArgsConstructor
public class DeptDto extends BaseDto {
    /**
     * 科室主键id
     */
    @ApiModelProperty(value="科室主键id")
    private Long deptId;

    /**
     * 科室名称
     */
    @NotNull(message = "科室名称不能为空")
    @ApiModelProperty(value="科室名称")
    private String deptName;

    /**
     * 挂号编号
     */
    @NotNull(message = "挂号数量不能为空")
    @ApiModelProperty(value="当前科室的挂号数量")
    private Integer regNumber;

    /**
     * 科室编号
     */
    @NotNull(message = "科室编号不能为空")
    @ApiModelProperty(value="科室编号")
    private String deptNumber;

    /**
     * 显示顺序
     */
    @NotNull(message = "显示顺序不能为空")
    @ApiModelProperty(value="显示顺序")
    private Integer orderNum;

    /**
     * 负责人
     */
    @ApiModelProperty(value="负责人")
    private String deptLeader;

    /**
     * 联系电话
     */
    @ApiModelProperty(value="联系电话")
    private String contactPhone;

    /**
     * 科室状态（0正常 1停用）
     */
    @ApiModelProperty(value="科室状态（0正常 1停用）")
    private String status;
}