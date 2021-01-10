package com.fclever.service.impl;

import javax.annotation.Resource;
import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fclever.domain.Patient;
import com.fclever.domain.PatientFile;
import com.fclever.dto.PatientDto;
import com.fclever.mapper.PatientFileMapper;
import com.fclever.mapper.PatientMapper;
import com.fclever.service.PatientService;
import com.fclever.vo.DataGridView;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

/**
@author Fclever
@create 2021-01-09 17:17
*/
@Service
public class PatientServiceImpl implements PatientService{

    @Autowired
    private PatientMapper patientMapper;

    @Autowired
    private PatientFileMapper patientFileMapper;

    /**
     * 分页查询所有患者信息
     * @return  返回结果
     */
    @Override
    public DataGridView listPatientForPage(PatientDto patientDto) {
        // 创建分页对象
        Page<Patient> page = new Page<>(patientDto.getPageNum(), patientDto.getPageSize());
        // 创建查询条件对象
        QueryWrapper<Patient> qw = new QueryWrapper<>();
        // 构建查询条件
        // 模糊匹配患者姓名
        qw.like(StringUtils.isNotBlank(patientDto.getName()), Patient.COL_NAME, patientDto.getName());
        // 模糊匹配手机号
        qw.like(StringUtils.isNotBlank(patientDto.getPhone()), Patient.COL_PHONE, patientDto.getPhone());
        // 模糊匹配身份证号
        qw.like(StringUtils.isNotBlank(patientDto.getIdCard()), Patient.COL_ID_CARD, patientDto.getIdCard());
        // 执行查询
        this.patientMapper.selectPage(page, qw);
        // 构建返回结果
        return new DataGridView(page.getTotal(), page.getRecords());
    }

    /**
     * 根据id查询对应患者信息
     * @param patientId 对应患者的患者主键id
     * @return  返回结果
     */
    @Override
    public Patient getPatientById(String patientId) {
        return this.patientMapper.selectById(patientId);
    }

    /**
     * 根据患者信息主键id查询患者档案信息
     * @param patientId 对应患者的患者主键id
     * @return  查询结果
     */
    @Override
    public PatientFile getPatientFileById(String patientId) {
        return this.patientFileMapper.selectById(patientId);
    }
}
