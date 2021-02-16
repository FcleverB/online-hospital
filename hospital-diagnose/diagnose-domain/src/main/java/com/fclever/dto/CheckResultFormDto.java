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
import java.io.Serializable;
import java.util.List;

/**
 * @author Fclever
 * @create 2021-02-16 13:29
 */
@ApiModel(value="com-fclever-dto-CheckResultFormDto")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CheckResultFormDto extends BaseDto {

    /**
     * 处方检查项ID
     */
    @ApiModelProperty(value="处方检查项ID")
    @NotBlank(message = "处方检查项id不能为空")
    private String itemId;

    /**
     * 检查结果描述
     */
    @ApiModelProperty(value="检查结果描述")
    @NotBlank(message = "检查结果描述不能为空")
    private String resultMsg;

    /**
     * 检查结果扫描附件[json表示]
     */
    @ApiModelProperty(value="检查结果扫描附件[json表示]")
    @NotBlank(message = "检查结果不能为空")
    private String resultImg;
}
