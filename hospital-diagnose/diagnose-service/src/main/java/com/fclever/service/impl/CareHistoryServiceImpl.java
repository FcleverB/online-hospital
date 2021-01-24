package com.fclever.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.fclever.constants.Constants;
import com.fclever.dto.CareHistoryDto;
import com.fclever.utils.IdGeneratorSnowflake;
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

    /**
     * 保存或更新病历信息
     *      如果病历id已经存在则进行更新
     * @param careHistoryDto    待保存数据
     * @return  返回结果
     */
    @Override
    public CareHistory saveOrUpdateCareHistory(CareHistoryDto careHistoryDto) {
        // 创建实体对象
        CareHistory careHistory = new CareHistory();
        // 值拷贝
        BeanUtil.copyProperties(careHistoryDto, careHistory);
        // 如果病历id存在，则进行修改，否则进行插入
        if (StringUtils.isNotBlank(careHistory.getChId())) {
            // 更新操作
            this.careHistoryMapper.updateById(careHistory);
        } else {
            // 添加
            careHistory.setChId(IdGeneratorSnowflake.generatorIdWithProfix(Constants.ID_PREFIX_CH));
            this.careHistoryMapper.insert(careHistory);
        }
        return careHistory;
    }
}
