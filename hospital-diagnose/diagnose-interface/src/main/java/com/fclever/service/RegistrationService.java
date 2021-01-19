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
}
