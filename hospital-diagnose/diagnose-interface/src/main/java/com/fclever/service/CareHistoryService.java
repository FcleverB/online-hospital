package com.fclever.service;

import com.fclever.domain.CareHistory;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fclever.dto.CareHistoryDto;

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

    /**
     * 保存或更新病历信息
     *      如果病历id已经存在则进行更新
     * @param careHistoryDto    待保存数据
     * @return  返回结果
     */
    CareHistory saveOrUpdateCareHistory(CareHistoryDto careHistoryDto);

    /**
     * 根据挂号单id查询对应的病历信息
     * @param registrationId    挂号单id
     * @return  返回结果
     */
    CareHistory queryCareHistoryByRegistrationId(String registrationId);

    /**
     * 根据病历id查询对应的病历信息
     * @param chId  病历id
     * @return  返回结果
     */
    CareHistory queryCareHistoryByChId(String chId);
}
