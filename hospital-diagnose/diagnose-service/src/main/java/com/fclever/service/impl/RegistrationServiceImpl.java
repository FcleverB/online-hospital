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
}
