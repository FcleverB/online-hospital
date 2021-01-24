package com.fclever.controller.diagnose;

import cn.hutool.core.date.DateUtil;
import com.fclever.aspectj.annotation.Log;
import com.fclever.aspectj.enums.BusinessType;
import com.fclever.constants.Constants;
import com.fclever.controller.BaseController;
import com.fclever.domain.*;
import com.fclever.dto.CareHistoryDto;
import com.fclever.service.CareHistoryService;
import com.fclever.service.DeptService;
import com.fclever.service.PatientService;
import com.fclever.service.RegistrationService;
import com.fclever.utils.DateUtils;
import com.fclever.utils.ShiroSecurityUtils;
import com.fclever.vo.AjaxResult;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 新开就诊控制层
 * @author Fclever
 * @create 2021-01-20 20:48
 */
@RestController
@RequestMapping("doctor/care")
public class CareController extends BaseController {

    @Reference
    private RegistrationService registrationService;

    @Reference
    private PatientService patientService;

    @Reference
    private CareHistoryService careHistoryService;

    @Autowired
    private DeptService deptService;

    /**
     * 查询待就诊的挂号信息
     * @param schedulingType    就诊类型  门诊|急诊
     * @return  返回结果
     */
    @GetMapping("queryToBeSeenRegistration/{schedulingType}")
    @HystrixCommand
    public AjaxResult queryToBeSeenRegistration(@PathVariable String schedulingType) {
        // 需要查询当前登录用户所在的科室id，只能接诊自己科室的患者挂号
        Long deptId = ShiroSecurityUtils.getCurrentUser().getDeptId();
        // 设置要查询的状态，只能查询挂号信息的待就诊状态信息
        String registrationStatus = Constants.REG_STATUS_1;
        // 设置默认可以选择挂号的时间段，根据当前时间进行计算，如果是上午进入，那么只能挂上午的号，下午-》下午，以此类推
        // 就诊中和就诊完成两个查询时，不需要根据当前时间计算时间段，因为已经接诊了，就任何时间都可以查看即可
        // 但是处于未接诊状态的时候，就可以指定根据当前时间来计算时间段，然后显示当前时间段的数据进行回显即可了
        String subsectionType = DateUtils.getCurrentTimeType();
        // 当前登录用户的id，待就诊的话，可以查看到当前科室的所有患者信息
        // 就诊中和就诊完成，只能看到当前用户（医生）的接诊的患者，其他同科室医生的接诊信息无法查询到
        Long userId = null;
        List<Registration> registrationList = this.registrationService.queryRegistration(schedulingType, deptId, registrationStatus, subsectionType, userId);
        return AjaxResult.success(registrationList);
    }

    /**
     * 查询就诊中的挂号信息
     * @param schedulingType    就诊类型  门诊|急诊
     * @return  返回结果
     */
    @GetMapping("queryVisitingRegistration/{schedulingType}")
    @HystrixCommand
    public AjaxResult queryVisitingRegistration(@PathVariable String schedulingType) {
        // 需要查询当前登录用户所在的科室id，只能接诊自己科室的患者挂号
        Long deptId = ShiroSecurityUtils.getCurrentUser().getDeptId();
        // 设置要查询的状态，只能查询挂号信息的就诊中状态信息
        String registrationStatus = Constants.REG_STATUS_2;
        // 当前登录用户的id，待就诊的话，可以查看到当前科室的所有患者信息
        // 就诊中和就诊完成，只能看到当前用户（医生）的接诊的患者，其他同科室医生的接诊信息无法查询到
        Long userId = ShiroSecurityUtils.getCurrentUser().getUserId();
        List<Registration> registrationList = this.registrationService.queryRegistration(schedulingType, deptId, registrationStatus, null, userId);
        return AjaxResult.success(registrationList);
    }

    /**
     * 查询就诊完成的挂号信息
     * @param schedulingType    就诊类型  门诊|急诊
     * @return  返回结果
     */
    @GetMapping("queryVisitCompleteRegistration/{schedulingType}")
    @HystrixCommand
    public AjaxResult queryVisitCompleteRegistration(@PathVariable String schedulingType) {
        // 需要查询当前登录用户所在的科室id，只能接诊自己科室的患者挂号
        Long deptId = ShiroSecurityUtils.getCurrentUser().getDeptId();
        // 设置要查询的状态，只能查询挂号信息的就诊完成状态信息
        String registrationStatus = Constants.REG_STATUS_3;
        // 当前登录用户的id，待就诊的话，可以查看到当前科室的所有患者信息
        // 就诊中和就诊完成，只能看到当前用户（医生）的接诊的患者，其他同科室医生的接诊信息无法查询到
        Long userId = ShiroSecurityUtils.getCurrentUser().getUserId();
        List<Registration> registrationList = this.registrationService.queryRegistration(schedulingType, deptId, registrationStatus, null, userId);
        return AjaxResult.success(registrationList);
    }

    /**
     * 医生接诊
     * @param registrationId    挂号信息id
     * @return  返回结果
     */
    @PutMapping("receivePatient/{registrationId}")
    @HystrixCommand
    @Log(title = "医生接诊", businessType = BusinessType.UPDATE)
    public AjaxResult receivePatient(@PathVariable String registrationId) {
        // 防止并发接诊
        synchronized (this) {
            // 根据id查询对应挂号信息
            Registration registration = this.registrationService.queryRegistrationById(registrationId);
            // 判断是否存在对应的挂号信息
            if(null==registration){
                return AjaxResult.fail("【"+registrationId+"】挂号单的不存在，不能接诊");
            }
            //只有当挂号单的状态 registrationStatus 待就诊时可以接诊
            if(registration.getRegistrationStatus().equals(Constants.REG_STATUS_1)) {
                registration.setRegistrationStatus(Constants.REG_STATUS_2);//就诊中
                registration.setUserId(ShiroSecurityUtils.getCurrentUser().getUserId());
                registration.setDoctorName(ShiroSecurityUtils.getCurrentUser().getUserName());
                registration.setUpdateTime(DateUtil.date());
                return AjaxResult.toAjax(this.registrationService.receivePatient(registration));
            }else{
                return AjaxResult.fail("【"+registrationId+"】挂号单的状态不是待就诊状态，不能接诊");
            }
        }
    }

    /**
     * 根据患者id查询患者信息、档案信息、病例信息
     * @param patientId 患者id
     * @return  返回结果
     */
    @GetMapping("getPatientAllMessageByPatientId/{patientId}")
    @HystrixCommand
    public AjaxResult getPatientAllMessageByPatientId(@PathVariable String patientId){
        //查询患者信息
        Patient patient=this.patientService.getPatientById(patientId);
        //查询档案
        PatientFile patientFile=this.patientService.getPatientFileById(patientId);
        //查询病历表
        List<CareHistory> careHistories=this.careHistoryService.queryCareHistoryByPatientId(patientId);
        // 封装查询结果
        Map<String,Object> res=new HashMap<>();
        res.put("patient",patient);
        res.put("patientFile",patientFile);
        res.put("careHistoryList",careHistories);
        return AjaxResult.success(res);
    }

    /**
     * 保存或更新病历信息
     *      如果病历id已经存在则进行更新
     * @param careHistoryDto    待保存数据
     * @return  返回结果
     */
    @PostMapping("saveCareHistory")
    @HystrixCommand
    @Log(title = "保存病历信息",businessType = BusinessType.INSERT)
    public AjaxResult saveCareHistory(@RequestBody CareHistoryDto careHistoryDto) {
        // 封装数据
        careHistoryDto.setUserId(ShiroSecurityUtils.getCurrentUser().getUserId());
        careHistoryDto.setUserName(ShiroSecurityUtils.getCurrentUser().getUserName());
        careHistoryDto.setDeptId(ShiroSecurityUtils.getCurrentUser().getDeptId());
        Dept dept = this.deptService.getDeptById(ShiroSecurityUtils.getCurrentUser().getDeptId());
        careHistoryDto.setDeptName(dept.getDeptName());
        // 设置就诊时间
        careHistoryDto.setCareTime(DateUtil.date());
        CareHistory careHistory = this.careHistoryService.saveOrUpdateCareHistory(careHistoryDto);
        return AjaxResult.success(careHistory);
    }
}
