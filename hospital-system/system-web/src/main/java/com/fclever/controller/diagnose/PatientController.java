package com.fclever.controller.diagnose;

import cn.hutool.core.bean.BeanUtil;
import com.fclever.controller.BaseController;
import com.fclever.domain.*;
import com.fclever.dto.PatientDto;
import com.fclever.service.CareHistoryService;
import com.fclever.service.CareOrderItemService;
import com.fclever.service.CareOrderService;
import com.fclever.service.PatientService;
import com.fclever.vo.AjaxResult;
import com.fclever.vo.DataGridView;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

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

    @Reference
    private CareHistoryService careHistoryService;

    @Reference
    private CareOrderService careOrderService;

    @Reference
    private CareOrderItemService careOrderItemService;

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
    @HystrixCommand
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
    @HystrixCommand
    public AjaxResult getPatientFileById(@PathVariable String patientId) {
        PatientFile patientFile = this.patientService.getPatientFileById(patientId);
        return AjaxResult.success(patientFile);
    }

    /**
     * 根据患者id查询对应的病历列表，查询对应的处方列表，查询对应的处方项目列表
     * @param patientId 患者id
     * @return  返回结果
     */
    @GetMapping("getPatientAllMessageByPatientId/{patientId}")
    @HystrixCommand
    public AjaxResult getPatientAllMessageByPatientId(@PathVariable String patientId) {
        // 根据患者id查询病历列表信息
        List<CareHistory> careHistories = this.careHistoryService.queryCareHistoryByPatientId(patientId);
        //构造返回的数据对象
        List<Map<String,Object>> res=new ArrayList<>();
        // 遍历每个病历对象
        for (CareHistory careHistory : careHistories) {
            // 将一个病历对象转成Map对象
            Map<String, Object> careHistoryMap = BeanUtil.beanToMap(careHistory);
            careHistoryMap.put("careOrders", Collections.EMPTY_LIST);
            // 保存该病历对象下的所有处方集合
            List<Map<String, Object>> reCareOrders = new ArrayList<>();
            //根据病历ID查询处方列表
            List<CareOrder> careOrders = this.careOrderService.queryCareOrdersByChId(careHistory.getChId());
            for (CareOrder order : careOrders) {
                // 遍历处方列表
                Map<String, Object> careOrder = BeanUtil.beanToMap(order);
                // 查询对应处方的所有处方详情信息
                List<CareOrderItem> careOrderItems = this.careOrderItemService.queryCareOrderItemsByCoId(order.getCoId());
                // 将处方详情信息放入到Map中
                careOrder.put("careOrderItems", careOrderItems);
                // 处方添加该处方详情
                reCareOrders.add(careOrder);
            }
            careHistoryMap.put("careOrders", reCareOrders);
            res.add(careHistoryMap);
        }
        return AjaxResult.success(res);
    }

}
