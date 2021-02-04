package com.fclever.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
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
 * 挂号信息实体Dto
 * @author Fclever
 * @create 2021-01-18 19:03
 */
@ApiModel(value="com-fclever-dto-RegistrationDto")
@Data
@EqualsAndHashCode(callSuper=true)
@AllArgsConstructor
@NoArgsConstructor
public class RegistrationDto extends BaseDto{

    /**
     * 挂号流水
     */
    @ApiModelProperty(value="挂号流水")
    private String registrationId;

    /**
     * 患者ID
     */
    @ApiModelProperty(value="患者ID")
    @NotBlank(message = "患者id不能为空")
    private String patientId;

    /**
     * 患者姓名
     */
    @ApiModelProperty(value="患者姓名")
    @NotBlank(message = "患者姓名不能为空")
    private String patientName;

    /**
     * 接诊医生ID
     */
    @ApiModelProperty(value="接诊医生ID")
    private Long userId;

    /**
     * 接诊医生姓名
     */
    @ApiModelProperty(value="接诊医生姓名")
    private String doctorName;

    /**
     * 科室ID
     */
    @ApiModelProperty(value="科室ID")
    @NotBlank(message = "科室ID不能为空")
    private Long deptId;

    /**
     * 挂号费用ID
     */
    @ApiModelProperty(value="挂号项目ID")
    @NotNull(message = "挂号项目ID不能为空")
    private Long registrationItemId;

    /**
     * 挂号总金额
     */
    @ApiModelProperty(value="挂号总金额")
    @NotBlank(message = "挂号总金额不能为空")
    private BigDecimal registrationAmount;

    /**
     * 挂号编号
     */
    @ApiModelProperty(value="挂号编号")
    private Integer registrationNumber;

    /**
     * 挂号状态0未收费,1待就诊，2,就诊中，3，就诊完成，4，已退号，5 作废
     */
    @ApiModelProperty(value="挂号状态0未收费,1待就诊，2,就诊中，3，就诊完成，4，已退号，5 作废")
    private String registrationStatus;

    /**
     * 就诊日期
     */
    @ApiModelProperty(value="就诊日期")
    @NotBlank(message = "挂号总金额不能为空")
    private String visitDate;

    /**
     * 排班类型1 门诊 2 急诊 字典表数据翻译
     */
    @ApiModelProperty(value="就诊类型1 门诊 2 急诊 字典表数据翻译")
    @NotBlank(message = "就诊类型不能为空")
    private String schedulingType;

    /**
     * 排班时段1上午  2下午 3晚上 字典表数据翻译
     */
    @ApiModelProperty(value="就诊时段1上午  2下午 3晚上 字典表数据翻译")
    @NotBlank(message = "就诊时段不能为空")
    private String subsectionType;
}
