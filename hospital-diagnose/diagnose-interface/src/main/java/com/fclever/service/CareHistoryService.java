package com.fclever.service;

import com.fclever.domain.CareHistory;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
@author Fclever
@create 2021-01-20 20:40
*/
public interface CareHistoryService{

    /**
     * 根据id查询患者的病历信息
     * @param patientId 患者id
     * @return  返回结果
     */
    List<CareHistory> queryCareHistoryByPatientId(String patientId);
}
