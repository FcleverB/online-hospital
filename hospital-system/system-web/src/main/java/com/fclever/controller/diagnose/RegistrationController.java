package com.fclever.controller.diagnose;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.fclever.aspectj.annotation.Log;
import com.fclever.aspectj.enums.BusinessType;
import com.fclever.constants.Constants;
import com.fclever.controller.BaseController;
import com.fclever.domain.Dept;
import com.fclever.domain.Patient;
import com.fclever.domain.Registration;
import com.fclever.dto.PatientDto;
import com.fclever.dto.RegistrationDto;
import com.fclever.dto.RegistrationFormDto;
import com.fclever.dto.RegistrationQueryDto;
import com.fclever.service.DeptService;
import com.fclever.service.PatientService;
import com.fclever.service.RegistrationService;
import com.fclever.service.SchedulingService;
import com.fclever.utils.IdGeneratorSnowflake;
import com.fclever.utils.ShiroSecurityUtils;
import com.fclever.vo.AjaxResult;
import com.fclever.vo.DataGridView;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.apache.dubbo.config.annotation.Reference;
import org.aspectj.weaver.loadtime.Aj;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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

    /**
     * 挂号工嗯呢该
     * @param registrationFormDto   需要保存的JSON信息
     * @return  返回结果
     */
    @PostMapping("addRegistration")
    @HystrixCommand
    @Log(title = "保存挂号信息",businessType = BusinessType.INSERT)
    public AjaxResult addRegistration(@RequestBody @Validated RegistrationFormDto registrationFormDto) {
        PatientDto patientDto = registrationFormDto.getPatientDto();
        RegistrationDto registrationDto = registrationFormDto.getRegistrationDto();
        // 之前的患者信息是根据身份证号查出来的，如果此时patientDto中没有患者id，那说明根据身份证号没有查出来患者信息，表示是录入的新的患者信息
        // 需要先添加患者信息
        Patient patient = null;
        if (StringUtils.isBlank(patientDto.getPatientId())){
            // 随机生成患者主键id
            patientDto.setPatientId(IdGeneratorSnowflake.generatorIdWithProfix(Constants.ID_PREFIX_HZ));
            // 设置创建人信息
            patientDto.setSimpleUser(ShiroSecurityUtils.getCurrentSimpleUser());
            // 调用patientService执行添加方法
            patient = patientService.addPatient(patientDto);
        } else {
            // 如果有患者编号，则根据编号查询患者信息
            patient = this.patientService.getPatientById(patientDto.getPatientId());

        }
        if (patient == null) {
            return AjaxResult.fail("当前患者id不存在，请确认后再进行挂号");
        }
        // 查询对应挂号的部门信息，获取挂号数，执行挂号操作时，在原有号数的基础上增加一
        Dept dept = this.deptService.getDeptById(registrationDto.getDeptId());
        // 保存挂号信息
        registrationDto.setSimpleUser(ShiroSecurityUtils.getCurrentSimpleUser());
        registrationDto.setRegistrationId(IdGeneratorSnowflake.generatorIdWithProfix(Constants.ID_PREFIX_GH));
        registrationDto.setPatientId(patient.getPatientId());
        registrationDto.setPatientName(patient.getName());
        registrationDto.setRegistrationStatus(Constants.REG_STATUS_0); // 待就诊
        registrationDto.setRegistrationNumber(dept.getRegNumber()+1);
        this.registrationService.addRegistration(registrationDto);
        // 更新对应科室的挂号数
        this.deptService.updateRegNumberByDeptId(dept.getDeptId(), dept.getRegNumber() + 1);
        // 返回挂号编号给前端
        return AjaxResult.success("挂号成功",registrationDto.getRegistrationId());
    }


    /**
     * 挂号收费
     * @param registrationId    挂号流水id
     * @return  返回结果
     */
    @PutMapping("charge/{registrationId}")
    @HystrixCommand
    @Log(title = "挂号收费",businessType = BusinessType.UPDATE)
    public AjaxResult charge(@PathVariable String registrationId) {
        // 查询挂号流水id对应的挂号单据信息
        Registration registration = this.registrationService.queryRegistrationById(registrationId);
        if (null == registration) {
            return AjaxResult.fail("当前挂号流水Id【"+registrationId+"】对应的挂号单数据不存在！");
        }
        // 如果挂号单对应的状态不是未收费
        if (!registration.getRegistrationStatus().equals(Constants.REG_STATUS_0)) {
            return AjaxResult.fail("当前挂号流水Id【"+registrationId+"】不是未收费状态，不可以进行收费！");
        }
        // 执行收费，更新挂号单状态
        registration.setRegistrationStatus(Constants.REG_STATUS_1);
        return AjaxResult.toAjax(this.registrationService.updateRegistrationById(registration));
    }

    /**
     * 分页查询挂号信息
     * @param registrationDto   前端传递的查询条件
     * @return  返回结果
     */
    @GetMapping("queryRegistrationForPage")
    @HystrixCommand
    public AjaxResult queryRegistrationForPage(RegistrationDto registrationDto) {
        // 执行分页查询的方法，并返回分页对象
        DataGridView dataGridView = this.registrationService.queryRegistrationForPage(registrationDto);
        return AjaxResult.success("分页数据查询成功",dataGridView.getData(), dataGridView.getTotal());
    }

    /**
     * 作废【根据挂号流水Id】
     * @param registrationId    挂号流水Id
     * @return  返回结果
     */
    @PutMapping("doInvalid/{registrationId}")
    @HystrixCommand
    @Log(title = "作废【根据挂号流水Id】",businessType = BusinessType.UPDATE)
    public AjaxResult doInvalid(@PathVariable String registrationId) {
        // 查询挂号流水id对应的挂号单据信息
        Registration registration = this.registrationService.queryRegistrationById(registrationId);
        if (null == registration) {
            return AjaxResult.fail("当前挂号流水Id【"+registrationId+"】对应的挂号单数据不存在！");
        }
        // 如果挂号单对应的状态不是未收费
        if (!registration.getRegistrationStatus().equals(Constants.REG_STATUS_0)) {
            return AjaxResult.fail("当前挂号流水Id【"+registrationId+"】不是未收费状态，不可以进行作废！");
        }
        // 设置状态为作废
        registration.setRegistrationStatus(Constants.REG_STATUS_5);
        return AjaxResult.toAjax(this.registrationService.doInvalid(registration));
    }

    /**
     * 退号【根据挂号流水号】
     * @param registrationId    挂号流水Id
     * @return  返回结果
     */
    @PutMapping("doReturn/{registrationId}")
    @HystrixCommand
    @Log(title = "退号【根据挂号流水号】",businessType = BusinessType.UPDATE)
    public AjaxResult doReturn(@PathVariable String registrationId) {
        // 查询挂号流水id对应的挂号单据信息
        Registration registration = this.registrationService.queryRegistrationById(registrationId);
        if (null == registration) {
            return AjaxResult.fail("当前挂号流水Id【"+registrationId+"】对应的挂号单数据不存在！");
        }
        // 如果挂号单对应的状态不是未收费
        if (!registration.getRegistrationStatus().equals(Constants.REG_STATUS_1)) {
            return AjaxResult.fail("当前挂号流水Id【"+registrationId+"】不是待就诊状态，不可以进行退号！");
        }
        // 设置状态为退号
        registration.setRegistrationStatus(Constants.REG_STATUS_4);
        return AjaxResult.toAjax(this.registrationService.doInvalid(registration));
    }
}

