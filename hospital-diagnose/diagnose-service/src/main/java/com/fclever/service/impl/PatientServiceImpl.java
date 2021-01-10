package com.fclever.service.impl;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fclever.domain.Patient;
import com.fclever.mapper.PatientMapper;
import com.fclever.service.PatientService;
/**
@author Fclever
@create 2021-01-09 17:17
*/
@Service
public class PatientServiceImpl extends ServiceImpl<PatientMapper, Patient> implements PatientService{

}
