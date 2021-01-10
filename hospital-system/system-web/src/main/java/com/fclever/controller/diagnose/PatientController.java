package com.fclever.controller.diagnose;

import com.fclever.controller.BaseController;
import com.fclever.domain.Patient;
import com.fclever.domain.PatientFile;
import com.fclever.dto.PatientDto;
import com.fclever.service.PatientService;
import com.fclever.vo.AjaxResult;
import com.fclever.vo.DataGridView;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 患者信息管理控制层
 * @author Fclever
 * @create 2021-01-10 15:13
 */
@RestController
@RequestMapping("doctor/patient")
public class PatientController extends BaseController {

    @Reference
    private PatientService patientService;

    /**
     * 分页查询所有患者信息
     * @return  返回结果
     */
    @GetMapping("listPatientForPage")
    @HystrixCommand
    public AjaxResult listPatientForPage(PatientDto patientDto) {
        DataGridView dataGridView = this.patientService.listPatientForPage(patientDto);
        return AjaxResult.success("分页数据查询成功",dataGridView.getData(),dataGridView.getTotal());
    }

    /**
     * 根据id查询对应患者信息
     * @param patientId 对应患者的患者主键id
     * @return  返回结果
     */
    @GetMapping("getPatientById/{patientId}")
    public AjaxResult getPatientById(@PathVariable String patientId) {
        Patient patient = this.patientService.getPatientById(patientId);
        return AjaxResult.success(patient);
    }

    /**
     * 根据患者信息主键id查询患者档案信息
     * @param patientId 对应患者的患者主键id
     * @return  查询结果
     */
    @GetMapping("getPatientFileById/{patientId}")
    public AjaxResult getPatientFileById(@PathVariable String patientId) {
        PatientFile patientFile = this.patientService.getPatientFileById(patientId);
        return AjaxResult.success(patientFile);
    }
}
