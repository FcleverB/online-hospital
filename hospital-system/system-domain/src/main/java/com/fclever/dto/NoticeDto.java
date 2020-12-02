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
@create 2020-12-02 23:39
*/

/**
    * 通知公告表
    */
@ApiModel(value="com-fclever-dto-NoticeDto")
@Data
@EqualsAndHashCode(callSuper=true)
@AllArgsConstructor
@NoArgsConstructor
public class NoticeDto extends BaseDto {
    /**
     * 通知ID
     */
    @ApiModelProperty(value="通知ID")
    private Integer noticeId;

    /**
     * 通知标题
     */
    @ApiModelProperty(value="通知标题")
    private String noticeTitle;

    /**
     * 通知类型（1通知 2公告）
     */
    @ApiModelProperty(value="通知类型（1通知 2公告）")
    private String noticeType;

    /**
     * 通知内容
     */
    @ApiModelProperty(value="通知内容")
    private String noticeContent;

    /**
     * 通知状态（0正常 1关闭）
     */
    @ApiModelProperty(value="通知状态（0正常 1关闭）")
    private String status;

    /**
     * 创建者
     */
    @TableField(value = "create_by")
    @ApiModelProperty(value="创建者")
    private String createBy;
}