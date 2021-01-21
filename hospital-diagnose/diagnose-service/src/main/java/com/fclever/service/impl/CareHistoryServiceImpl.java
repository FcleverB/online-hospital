package com.fclever.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import com.fclever.domain.CareHistory;
import com.fclever.mapper.CareHistoryMapper;
import com.fclever.service.CareHistoryService;
/**
@author Fclever
@create 2021-01-20 20:40
*/
@Service
public class CareHistoryServiceImpl implements CareHistoryService{

    @Autowired
    private CareHistoryMapper careHistoryMapper;

    /**
     * 根据id查询患者的病历信息
     * @param patientId 患者id
     * @return  返回结果
     */
    @Override
    public List<CareHistory> queryCareHistoryByPatientId(String patientId) {
        QueryWrapper<CareHistory> qw = new QueryWrapper<>();
        qw.eq(patientId != null, CareHistory.COL_PATIENT_ID, patientId);
        qw.orderByAsc(CareHistory.COL_CARE_TIME);
        return this.careHistoryMapper.selectList(qw);
    }
}
