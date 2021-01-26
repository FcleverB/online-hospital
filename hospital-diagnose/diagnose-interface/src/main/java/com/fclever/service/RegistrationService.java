package com.fclever.service;

import com.fclever.domain.Registration;
import com.fclever.domain.SimpleUser;
import com.fclever.dto.RegistrationDto;
import com.fclever.vo.DataGridView;

import java.util.List;

/**
@author Fclever
@create 2021-01-14 13:40
*/
public interface RegistrationService{

    /**
     * 保存挂号信息
     * @param registrationDto   挂号数据
     */
    void addRegistration(RegistrationDto registrationDto);

    /**
     * 根据挂号流水Id查询对应的挂号信息
     * @param registrationId    挂号流水Id
     * @return  返回结果
     */
    Registration queryRegistrationById(String registrationId);

    /**
     * 更新收费状态
     * 医生接诊---更新数据状态
     * @param registration 待更新的数据
     * @return  返回结果
     */
    int updateRegistrationById(Registration registration);

    /**
     * 作废【根据挂号流水Id】
     * @param registration    待更新的数据
     * @return  返回结果
     */
    int doInvalid(Registration registration);

    /**
     * 退号【根据挂号流水号】
     * @param registration    待更新的数据
     * @return  返回结果
     */
    int doReturn(Registration registration);

    /**
     * 分页查询挂号信息
     * @param registrationDto   前端传递的查询条件
     * @return  返回结果
     */
    DataGridView queryRegistrationForPage(RegistrationDto registrationDto);

    /**
     * 医生接诊
     * @param registration  挂号信息
     * @return  返回结果
     */
    int receivePatient(Registration registration);

    /**
     * 查询待就诊的挂号信息
     * @param schedulingType        挂号类型    门诊|急诊
     * @param deptId                科室id
     * @param registrationStatus    挂号状态
     * @param subsectionType        挂号时段
     * @param userId                医生id
     * @return  返回结果
     */
    List<Registration> queryRegistration(String schedulingType, Long deptId, String registrationStatus, String subsectionType, Long userId);

    /**
     * 完成就诊
     * @param registrationId    待修改的挂号单据id
     * @return  返回结果
     */
    int visitComplete(String registrationId);
}
