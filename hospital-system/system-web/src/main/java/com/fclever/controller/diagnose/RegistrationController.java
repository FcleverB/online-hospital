package com.fclever.controller.diagnose;

import com.fclever.controller.BaseController;
import com.fclever.domain.Dept;
import com.fclever.domain.Patient;
import com.fclever.dto.RegistrationQueryDto;
import com.fclever.service.DeptService;
import com.fclever.service.PatientService;
import com.fclever.service.RegistrationService;
import com.fclever.service.SchedulingService;
import com.fclever.vo.AjaxResult;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

/**
 * 挂号功能控制层
 * @author Fclever
 * @create 2021-01-15 10:10
 */
@RestController
@RequestMapping("doctor/registration")
public class RegistrationController extends BaseController {

    @Reference
    private RegistrationService registrationService;

    @Reference
    private SchedulingService schedulingService;

    @Autowired
    private DeptService deptService;

    @Reference
    private PatientService patientService;


    /**
     * 分页查询部门列表信息
     *      1. 根据查询条件从his_scheduling表中查询出去重后的科室id集合
     *      2. 然后根据这些科室id集合再去sys_dept表中查询出科室id，科室名称，挂号数量这些信息，并返回给前台页面
     * @param registrationQueryDto  分页查询的查询条件
     * @return  返回结果
     */
    @GetMapping("listDeptForScheduling")
    @HystrixCommand
    public AjaxResult listDeptForScheduling(@Validated RegistrationQueryDto registrationQueryDto) {
        // 调用排班服务，查询科室id集合
        List<Long> deptIds = this.schedulingService.selectDeptIdsByQuery(registrationQueryDto);
        if (null != deptIds && deptIds.size() > 0) {
            // 根据科室id，查询对应的分页科室信息并返回
            List<Dept> deptList = this.deptService.listDeptByDeptIds(deptIds);
            return AjaxResult.success(deptList);
        }
        // 返回一个空的集合
        return AjaxResult.success(Collections.EMPTY_LIST);
    }

    /**
     * 根据身份证号查询患者信息
     * @param idCard 身份证号
     * @return 患者信息
     */
    @GetMapping("getPatientByIdCard/{idCard}")
    @HystrixCommand
    public AjaxResult getPatientByIdCard(@PathVariable String idCard) {
        Patient patient = this.patientService.getPatientByIdCard(idCard);
        if (patient == null) {
            return AjaxResult.fail("【"+idCard+"】对应的患者不存在，请在下面新建患者信息");
        }
        return AjaxResult.success(patient);
    }
}
