package com.fclever.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fclever.domain.Registration;
import com.fclever.domain.SimpleUser;
import com.fclever.dto.RegistrationDto;
import com.fclever.mapper.RegistrationMapper;
import com.fclever.service.RegistrationService;
import com.fclever.vo.DataGridView;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
@author Fclever
@create 2021-01-14 13:40
*/
@Service
public class RegistrationServiceImpl implements RegistrationService{

    @Autowired
    private RegistrationMapper registrationMapper;

    /**
     * 保存挂号信息
     * @param registrationDto   挂号数据
     */
    @Override
    public void addRegistration(RegistrationDto registrationDto) {
        // 创建实体对象
        Registration registration = new Registration();
        // 值拷贝
        BeanUtil.copyProperties(registrationDto, registration);
        // 设置值
        registration.setCreateBy(registrationDto.getSimpleUser().getUserName());
        registration.setCreateTime(DateUtil.date());
        // 执行插入操作
        this.registrationMapper.insert(registration);
    }

    /**
     * 根据挂号流水Id查询对应的挂号信息
     * @param registrationId    挂号流水Id
     * @return  返回结果
     */
    @Override
    public Registration queryRegistrationById(String registrationId) {
        return this.registrationMapper.selectById(registrationId);
    }

    /**
     * 更新收费状态
     * @param registration 待更新的数据
     * @return  返回结果
     */
    @Override
    public int updateRegistrationById(Registration registration) {
        return this.registrationMapper.updateById(registration);
    }

    /**
     * 作废【根据挂号流水Id】
     * @param registration    待更新的数据
     * @return  返回结果
     */
    @Override
    public int doInvalid(Registration registration) {
        // 执行更新操作
        return this.registrationMapper.updateById(registration);
    }

    /**
     * 退号【根据挂号流水号】
     * @param registration    待更新的数据
     * @return  返回结果
     */
    @Override
    public int doReturn(Registration registration) {
        // 执行更新操作
        return this.registrationMapper.updateById(registration);
    }

    /**
     * 分页查询挂号信息
     * @param registrationDto   前端传递的查询条件
     * @return  返回结果
     */
    @Override
    public DataGridView queryRegistrationForPage(RegistrationDto registrationDto) {
        // 构建分页对象
        Page<Registration> page = new Page<>(registrationDto.getPageNum(), registrationDto.getPageSize());
        // 封装查询条件
        QueryWrapper<Registration> qw = new QueryWrapper<>();
        // 精确匹配科室
        qw.eq(registrationDto.getDeptId() != null, Registration.COL_DEPT_ID, registrationDto.getDeptId());
        // 模糊匹配患者名称
        qw.like(StringUtils.isNotBlank(registrationDto.getPatientName()), Registration.COL_PATIENT_NAME, registrationDto.getPatientName());
        // 精确匹配挂号类型
        qw.eq(StringUtils.isNotBlank(registrationDto.getSchedulingType()), Registration.COL_SCHEDULING_TYPE, registrationDto.getSchedulingType());
        // 精确匹配挂号时段
        qw.eq(StringUtils.isNotBlank(registrationDto.getSubsectionType()), Registration.COL_SUBSECTION_TYPE, registrationDto.getSubsectionType());
        // 精确匹配挂号状态
        qw.eq(StringUtils.isNotBlank(registrationDto.getRegistrationStatus()), Registration.COL_REGISTRATION_STATUS, registrationDto.getRegistrationStatus());
        // 精确匹配时间
        qw.eq(StringUtils.isNotBlank(registrationDto.getVisitDate()), Registration.COL_VISIT_DATE, registrationDto.getVisitDate());
        // 设置数据显示顺序
        qw.orderByAsc(Registration.COL_VISIT_DATE);
        // 执行查询
        this.registrationMapper.selectPage(page, qw);
        // 封装DataGridView对象并返回
        return new DataGridView(page.getTotal(), page.getRecords());
    }

    /**
     * 医生接诊
     * @param registration  挂号信息
     * @return  返回结果
     */
    @Override
    public int receivePatient(Registration registration) {
        return this.registrationMapper.updateById(registration);
    }

    /**
     * 查询待就诊的挂号信息
     * @param schedulingType        挂号类型    门诊|急诊
     * @param deptId                科室id
     * @param registrationStatus    挂号状态
     * @param subsectionType        挂号时段
     * @param userId                医生id
     * @return  返回结果
     */
    @Override
    public List<Registration> queryRegistration(String schedulingType, Long deptId, String registrationStatus, String subsectionType, Long userId) {
        // 构建查询条件对象
        QueryWrapper<Registration> qw = new QueryWrapper<>();
        // 封装查询条件
        qw.eq(Registration.COL_SCHEDULING_TYPE, schedulingType);
        qw.eq(Registration.COL_DEPT_ID, deptId);
        qw.eq(Registration.COL_REGISTRATION_STATUS, registrationStatus);
        qw.eq(Registration.COL_SUBSECTION_TYPE, subsectionType);
        qw.eq(userId != null, Registration.COL_USER_ID, userId);
        // 排序
        qw.orderByAsc(Registration.COL_REGISTRATION_NUMBER);
        // 执行查询
        return this.registrationMapper.selectList(qw);
    }
}
