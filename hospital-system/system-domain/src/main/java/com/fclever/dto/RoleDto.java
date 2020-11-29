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
@create 2020-11-29 11:19
*/

/**
    * 角色信息表  数据传输，封住
    */
@ApiModel(value="com-fclever-dto-RoleDto")
@Data
@EqualsAndHashCode(callSuper=true)
@AllArgsConstructor
@NoArgsConstructor
public class RoleDto extends BaseDto {
    /**
     * 角色ID
     */
    @ApiModelProperty(value="角色ID")
    private Long roleId;

    /**
     * 角色名称
     */
    @NotNull(message = "角色名称不能为空")
    @ApiModelProperty(value="角色名称")
    private String roleName;

    /**
     * 角色码值
     */
    @NotNull(message = "角色码值不能为空")
    @ApiModelProperty(value="角色码值")
    private String roleCode;

    /**
     * 显示顺序
     */
    @NotNull(message = "显示顺序不能为空")
    @ApiModelProperty(value="显示顺序")
    private Integer roleSort;

    /**
     * 备注
     */
    @ApiModelProperty(value="备注")
    private String remark;

    /**
     * 角色状态（0正常 1停用）
     */
    @NotNull(message = "角色状态不能为空")
    @ApiModelProperty(value="角色状态（0正常 1停用）")
    private String status;

    /**
     * 删除标志（0代表存在 2代表删除）
     */
    @ApiModelProperty(value="删除标志（0代表存在 2代表删除）")
    private String delFlag;
}