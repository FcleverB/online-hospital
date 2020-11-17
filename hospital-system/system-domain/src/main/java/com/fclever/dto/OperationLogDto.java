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

import java.util.Date;

/**
@author Fclever
@create 2020-11-16 08:32
*/

/**
    * 操作日志记录
 *          和前端交互使用  封装查询条件
    */
@ApiModel(value="com-fclever-domain-OperationLog")
@Data
@EqualsAndHashCode(callSuper=true)
@AllArgsConstructor
@NoArgsConstructor
public class OperationLogDto extends BaseDto {

    /**
     * 模块标题
     */
    @TableField(value = "title")
    private String title;

    /**
     * 业务类型（0其它 1新增 2修改 3删除）
     */
    @TableField(value = "business_type")
    private String businessType;

    /**
     * 操作人员
     */
    @TableField(value = "oper_name")
    private String operName;

    /**
     * 操作状态（0正常 1异常）
     */
    @TableField(value = "status")
    private String status;
}