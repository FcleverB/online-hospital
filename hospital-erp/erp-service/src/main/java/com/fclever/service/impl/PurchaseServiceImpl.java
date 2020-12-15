package com.fclever.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fclever.constants.Constants;
import com.fclever.domain.Purchase;
import com.fclever.domain.SimpleUser;
import com.fclever.dto.PurchaseDto;
import com.fclever.mapper.PurchaseMapper;
import com.fclever.service.PurchaseService;
import com.fclever.vo.DataGridView;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

/**
@author Fclever
@create 2020-12-14 15:21
*/
@Service
public class PurchaseServiceImpl implements PurchaseService{

    @Autowired
    private PurchaseMapper purchaseMapper;

    /**
     * 分页查询所有采购入库列表数据
     * @param purchaseDto   查询条件
     * @return  返回结果
     */
    @Override
    public DataGridView listPurchaseForPage(PurchaseDto purchaseDto) {
        // 创建分页对象
        Page<Purchase> page = new Page<>(purchaseDto.getPageNum(), purchaseDto.getPageSize());
        // 创建查询条件对象
        QueryWrapper<Purchase> qw = new QueryWrapper<>();
        // 封装查询条件
        // 供应商精确匹配
        qw.eq(StringUtils.isNotBlank(purchaseDto.getProviderId()), Purchase.COL_PROVIDER_ID, purchaseDto.getProviderId());
        // 申请人模糊
        qw.like(StringUtils.isNotBlank(purchaseDto.getApplyUserName()), Purchase.COL_APPLY_USER_NAME, purchaseDto.getApplyUserName());
        // 状态精确
        qw.eq(StringUtils.isNotBlank(purchaseDto.getStatus()), Purchase.COL_STATUS, purchaseDto.getStatus());
        // 创建时间降序
        qw.orderByDesc(Purchase.COL_CREATE_TIME);
        // 执行查询
        this.purchaseMapper.selectPage(page, qw);
        // 封装分页对象并返回
        return new DataGridView(page.getTotal(), page.getRecords());
    }

    /**
     * 提交审核【根据入库单据id】
     *      状态为未提交和审核不通过可以进行该操作
     * @param purchaseId    需要提交的入库单据id
     * @param currentSimpleUser 当前登录人信息作为申请人信息的保存来源
     * @return  返回结果
     */
    @Override
    public int doAudit(String purchaseId, SimpleUser currentSimpleUser) {
        // 根据id查询入库单据实体，并赋值
        Purchase purchase = this.purchaseMapper.selectById(purchaseId);
        // 设置状态为待审核
        purchase.setStatus(Constants.STOCK_PURCHASE_STATUS_2);
        // 设置申请人名称和id
        purchase.setApplyUserName(currentSimpleUser.getUserName());
        purchase.setApplyUserId(Long.valueOf(currentSimpleUser.getUserId().toString()));
        return this.purchaseMapper.updateById(purchase);
    }

    /**
     * 作废【根据入库单据id】
     *      状态为未提交或者审核不通过才可以进行作废
     * @param purchaseId    需要作废的入库单据id
     * @return
     */
    @Override
    public int doInvalid(String purchaseId) {
        // 根据id查询入库单据实体，并赋值
        Purchase purchase = this.purchaseMapper.selectById(purchaseId);
        // 设置状态为作废
        purchase.setStatus(Constants.STOCK_PURCHASE_STATUS_5);
        return this.purchaseMapper.updateById(purchase);
    }

    /**
     * 审核通过【根据入库单据id】
     *      状态必须是待审核状态才能进行操作
     * @param purchaseId    需要审核通过的入库单据id
     * @return  返回结果
     */
    @Override
    public int auditPass(String purchaseId) {
        // 根据id查询入库单据实体，并赋值
        Purchase purchase = this.purchaseMapper.selectById(purchaseId);
        // 设置状态为审核通过
        purchase.setStatus(Constants.STOCK_PURCHASE_STATUS_3);
        return this.purchaseMapper.updateById(purchase);
    }

    /**
     * 审核不通过【根据入库单据id】
     *      状态必须为待审核状态
     * @param purchaseId    需要审核不通过的入库单据id
     * @param auditMsg   审核不通过信息
     * @return  返回结果
     */
    @Override
    public int auditRefuse(String purchaseId, String auditMsg) {
        // 根据id查询入库单据实体，并赋值
        Purchase purchase = this.purchaseMapper.selectById(purchaseId);
        // 设置状态为审核不通过
        purchase.setStatus(Constants.STOCK_PURCHASE_STATUS_4);
        // 设置不通过信息
        purchase.setAuditMsg(auditMsg);
        return this.purchaseMapper.updateById(purchase);
    }

    /**
     * 根据入库单据id查询对应的入库单据实体
     * @param purchaseId    待查询的入库单据id
     * @return  返回结果
     */
    @Override
    public Purchase getPurchaseById(String purchaseId) {
        return this.purchaseMapper.selectById(purchaseId);
    }
}
