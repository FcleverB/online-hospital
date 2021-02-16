package com.fclever.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fclever.constants.Constants;
import com.fclever.domain.CareOrderItem;
import com.fclever.domain.CheckResult;
import com.fclever.domain.OrderChargeItem;
import com.fclever.dto.CheckResultDto;
import com.fclever.dto.CheckResultFormDto;
import com.fclever.mapper.CareOrderItemMapper;
import com.fclever.mapper.CheckResultMapper;
import com.fclever.mapper.OrderChargeItemMapper;
import com.fclever.mapper.OrderChargeMapper;
import com.fclever.service.CareOrderItemService;
import com.fclever.service.CheckResultService;
import com.fclever.service.OrderChargeService;
import com.fclever.vo.DataGridView;
import org.apache.dubbo.config.annotation.Method;
import org.apache.dubbo.config.annotation.Reference;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
@author Fclever
@create 2021-02-15 13:11
*/
@Service(methods = {@Method(name = "startCheck", retries = 1)})
public class CheckResultServiceImpl implements CheckResultService{

    @Autowired
    private CheckResultMapper checkResultMapper;

    @Autowired
    private OrderChargeItemMapper orderChargeItemMapper;

    @Autowired
    private CareOrderItemMapper careOrderItemMapper;

    /**
     * 开始检查的方法
     * @param checkResult   待保存的数据
     */
    @Override
    @Transactional
    public void startCheck(CheckResult checkResult) {
        this.checkResultMapper.insert(checkResult);
        // 更新收费详情状态 已完成
        OrderChargeItem orderChargeItem = new OrderChargeItem();
        orderChargeItem.setItemId(checkResult.getItemId());
        orderChargeItem.setStatus(Constants.ORDER_DETAILS_STATUS_3);
        this.orderChargeItemMapper.updateById(orderChargeItem);
        // 更新处方详情状态
        CareOrderItem careOrderItem = new CareOrderItem();
        careOrderItem.setItemId(checkResult.getItemId());
        careOrderItem.setStatus(Constants.ORDER_DETAILS_STATUS_3);
        this.careOrderItemMapper.updateById(careOrderItem);
    }

    /**
     * 查询所有检查中的项目
     * @param checkResultDto    查询条件
     * @return  返回结果
     */
    @Override
    public DataGridView queryAllCheckingResultForPage(CheckResultDto checkResultDto) {
        // 创建分页对象
        Page<CheckResult> page = new Page<>(checkResultDto.getPageNum(), checkResultDto.getPageSize());
        // 创建查询条件对象
        QueryWrapper<CheckResult> qw = new QueryWrapper<>();
        // 封装查询条件
        qw.eq(CheckResult.COL_RESULT_STATUS, Constants.CHECK_RESULT_STATUS_0);
        qw.eq(StringUtils.isNotBlank(checkResultDto.getRegistrationId()), CheckResult.COL_REGISTRATION_ID, checkResultDto.getRegistrationId());
        qw.like(StringUtils.isNotBlank(checkResultDto.getPatientName()), CheckResult.COL_PATIENT_NAME, checkResultDto.getPatientName());
        qw.in(!checkResultDto.getCheckItemIds().isEmpty(), CheckResult.COL_CHECK_ITEM_ID, checkResultDto.getCheckItemIds());
        this.checkResultMapper.selectPage(page, qw);
        return new DataGridView(page.getTotal(), page.getRecords());
    }

    /**
     * 完成检查
     * @param checkResultFormDto    待更新的数据
     * @return  返回结果
     */
    @Override
    public int completeCheckResult(CheckResultFormDto checkResultFormDto) {
        // 创建实体对象
        CheckResult checkResult = new CheckResult();
        // 值拷贝
        BeanUtil.copyProperties(checkResultFormDto, checkResult);
        checkResult.setResultStatus(Constants.CHECK_RESULT_STATUS_1);
        checkResult.setUpdateBy(checkResultFormDto.getSimpleUser().getUserName());
        return this.checkResultMapper.updateById(checkResult);
    }
}
