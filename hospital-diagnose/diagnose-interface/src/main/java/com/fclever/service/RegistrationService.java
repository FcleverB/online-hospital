package com.fclever.service;

import com.fclever.domain.Registration;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fclever.dto.RegistrationDto;

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
     * @param registration 待更新的数据
     * @return  返回结果
     */
    int updateRegistrationById(Registration registration);
}
