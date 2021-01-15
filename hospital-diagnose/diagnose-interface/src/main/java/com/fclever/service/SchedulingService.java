package com.fclever.service;

import com.fclever.domain.Scheduling;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fclever.dto.RegistrationQueryDto;
import com.fclever.dto.SchedulingFormDto;
import com.fclever.dto.SchedulingQueryDto;

import java.util.List;

/**
@author Fclever
@create 2021-01-05 12:25
*/
public interface SchedulingService{

    /**
     * 根据条件查询符合的排班信息
     * @param schedulingQueryDto
     * @return 查询到的排班数据列表
     */
    List<Scheduling> queryScheduling(SchedulingQueryDto schedulingQueryDto);

    /**
     * 保存排班信息
     * @param schedulingFormDto  待保存的排班数据  传递的是Json对象
     * @return 返回结果
     */
    int saveScheduling(SchedulingFormDto schedulingFormDto);

    /**
     * 分页查询部门列表信息
     *      1. 根据查询条件从his_scheduling表中查询出去重后的科室id集合
     *      2. 然后根据这些科室id集合再去sys_dept表中查询出科室id，科室名称，挂号数量这些信息，并返回给前台页面
     * @param registrationQueryDto  分页查询的查询条件
     * @return  返回结果
     */
    List<Long> selectDeptIdsByQuery(RegistrationQueryDto registrationQueryDto);
}
