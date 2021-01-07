package com.fclever.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;

/**
 * 封装数据，封装排班列表中要显示的数据Dto
 * @author Fclever
 * @create 2021-01-06 10:29
 */
@ApiModel(value="com-fclever-dto-SchedulingDto")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SchedulingDto implements Serializable {

    // 用户id
    private Long userId;

    // 科室id
    private Long deptId;

    // 排班时段 上午1 下午2 晚上3
    private String subsectionType;

    // 排班类型 1 门诊 2 急诊
    // 存放对应subsectionType时间段的一周的排班类型
    private Collection<String> schedulingType;

    // 存放星期值班的记录  key：周一到周日的日期字符串  value：有值就是1和2，没有值就是“”
    @JsonIgnore
    private Map<String, String> record;

    /**
     * 构造器
     * @param userId    用户ID
     * @param deptId    科室id
     * @param subsectionType    排班时段
     * @param record    存放排班记录
     */
    public SchedulingDto(Long userId, Long deptId, String subsectionType, Map<String, String> record) {
        this.userId = userId;
        this.deptId = deptId;
        this.subsectionType = subsectionType;
        this.record = record;
    }
}
