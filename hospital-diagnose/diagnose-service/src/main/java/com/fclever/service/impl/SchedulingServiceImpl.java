package com.fclever.service.impl;

import javax.annotation.Resource;
import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fclever.domain.Scheduling;
import com.fclever.dto.SchedulingQueryDto;
import com.fclever.mapper.SchedulingMapper;
import com.fclever.service.SchedulingService;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

/**
@author Fclever
@create 2021-01-05 12:25
*/
@Service // 更换为Dubbo的Service，作为服务交给Dubbo管理
public class SchedulingServiceImpl implements SchedulingService{

    @Autowired
    private SchedulingMapper schedulingMapper;

    /**
     * 根据条件查询符合的排班信息
     * @param schedulingQueryDto
     * @return 查询到的排班数据列表
     */
    @Override
    public List<Scheduling> queryScheduling(SchedulingQueryDto schedulingQueryDto) {
        // 创建查询条件对象
        QueryWrapper<Scheduling> qw = new QueryWrapper<>();
        // 精确查询用户id
        qw.eq(schedulingQueryDto.getUserId() != null, Scheduling.COL_USER_ID, schedulingQueryDto.getUserId());
        // 精确查询科室id
        qw.eq(schedulingQueryDto.getDeptId() != null, Scheduling.COL_DEPT_ID, schedulingQueryDto.getDeptId());
        // 范围匹配周日期
        qw.ge(StringUtils.isNotBlank(schedulingQueryDto.getBeginDate()), Scheduling.COL_SCHEDULING_DAY, schedulingQueryDto.getBeginDate());
        qw.le(StringUtils.isNotBlank(schedulingQueryDto.getEndDate()), Scheduling.COL_SCHEDULING_DAY, schedulingQueryDto.getEndDate());
        // 执行查询List
        return this.schedulingMapper.selectList(qw);
    }
}
