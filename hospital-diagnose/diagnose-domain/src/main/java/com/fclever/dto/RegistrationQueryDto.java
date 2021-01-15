package com.fclever.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 挂号功能中科室分页查询的查询条件封装类
 * @author Fclever
 * @create 2021-01-15 10:15
 */
@ApiModel(value="com-fclever-dto-RegistrationQueryDto")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegistrationQueryDto implements Serializable {

    /**
     * 科室ID
     */
    @ApiModelProperty(value="科室ID")
    private Long deptId;

    /**
     * 排班日期
     */
    @ApiModelProperty(value = "排班日期")
    @NotBlank(message = "排班日期不能为空")
    private String schedulingDay;

    /**
     * 排班类型1 门诊 2 急诊 字典表数据翻译
     */
    @ApiModelProperty(value="排班类型1 门诊 2 急诊 字典表数据翻译")
    @NotBlank(message = "排班类型不能为空")
    private String schedulingType;

    /**
     * 排班时段1上午  2下午 3晚上 字典表数据翻译
     */
    @ApiModelProperty(value="排班时段1上午  2下午 3晚上 字典表数据翻译")
    @NotBlank(message = "排班时段不能为空")
    private String subsectionType;
}
