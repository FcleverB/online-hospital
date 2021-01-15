package com.fclever.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fclever.mapper.RegistrationMapper;
import com.fclever.domain.Registration;
import com.fclever.service.RegistrationService;
import org.apache.dubbo.config.annotation.Service;

/**
@author Fclever
@create 2021-01-14 13:40
*/
@Service
public class RegistrationServiceImpl extends ServiceImpl<RegistrationMapper, Registration> implements RegistrationService{

}
