package com.fclever.dto;

import com.fclever.domain.SimpleUser;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * 保存排班信息Dto
 *      封装页面中编辑的数据
 * @author Fclever
 * @create 2021-01-07 10:49
 */
@ApiModel(value="com-fclever-dto-SchedulingFormDto")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SchedulingFormDto implements Serializable {

    // 登录用户信息
    private SimpleUser simpleUser;

    // 当前周的开始时间
    private String beginDate;

    // 列表数据
    private List<SchedulingData> data;

    @Data
    public static class SchedulingData implements  Serializable{
        // 对应的医生id
        private Long userId;
        // 对应的医生科室id
        private Long deptId;
        // 排班时段
        private String subsectionType;
        // 对应排班时段的排班类型
        private Collection<String> schedulingType;
    }
}
