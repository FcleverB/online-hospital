package com.fclever.dto;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 封装查询条件的Dto
 * @author Fclever
 * @create 2021-01-05 12:29
 */
@ApiModel(value="com-fclever-dto-SchedulingQueryDto")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SchedulingQueryDto implements Serializable {

    // 查询的部门id
    private Long deptId;

    // 查询的用户id（医生id）
    private Long userId;

    // 前端传递的上一周或者下一周的日期值
    private String queryDate;

    // 根据传递的日期值计算后的对应所在周的开始日期
    private String beginDate;

    // 根据传递的日期值计算后的对应所在周的结束日期
    private String endDate;
}
