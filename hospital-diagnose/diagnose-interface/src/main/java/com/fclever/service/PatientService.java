package com.fclever.service;

import com.fclever.domain.Patient;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fclever.domain.PatientFile;
import com.fclever.dto.PatientDto;
import com.fclever.vo.DataGridView;

/**
@author Fclever
@create 2021-01-09 17:17
*/
public interface PatientService{

    /**
     * 分页查询所有患者信息
     * @return  返回结果
     */
    DataGridView listPatientForPage(PatientDto patientDto);

    /**
     * 根据id查询对应患者信息
     * @param patientId 对应患者的患者主键id
     * @return  返回结果
     */
    Patient getPatientById(String patientId);

    /**
     * 根据患者信息主键id查询患者档案信息
     * @param patientId 对应患者的患者主键id
     * @return  查询结果
     */
    PatientFile getPatientFileById(String patientId);

    /**
     * 根据身份证号查询患者信息
     * @param idCard 身份证号
     * @return 患者信息
     */
    Patient getPatientByIdCard(String idCard);

    /**
     * 添加患者信息
     *      在门诊挂号时，如果根据身份证号查询不到对应的患者信息，那么在手动输入患者信息之后
     *      进行挂号操作的时候，就可以自动保存患者信息
     * @param patientDto 待保存的患者信息
     * @return  返回结果
     */
    Patient addPatient(PatientDto patientDto);
}
