package com.fclever.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import com.fclever.domain.Registration;
import com.fclever.dto.RegistrationDto;
import com.fclever.mapper.RegistrationMapper;
import com.fclever.service.RegistrationService;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

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
}
