package com.fclever.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fclever.constants.Constants;
import com.fclever.domain.*;
import com.fclever.dto.PurchaseDto;
import com.fclever.dto.PurchaseFormDto;
import com.fclever.dto.PurchaseItemDto;
import com.fclever.mapper.InventoryLogMapper;
import com.fclever.mapper.MedicinesMapper;
import com.fclever.mapper.PurchaseItemMapper;
import com.fclever.mapper.PurchaseMapper;
import com.fclever.service.PurchaseService;
import com.fclever.utils.IdGeneratorSnowflake;
import com.fclever.vo.DataGridView;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
@author Fclever
@create 2020-12-14 15:21
*/
@Service
@Transactional
public class PurchaseServiceImpl implements PurchaseService{

    @Autowired
    private PurchaseMapper purchaseMapper;

    @Autowired
    private PurchaseItemMapper purchaseItemMapper;

    @Autowired
    private InventoryLogMapper inventoryLogMapper;

    @Autowired
    private MedicinesMapper medicinesMapper;

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

    /**
     * 暂存入库单据和详情信息--新增和详情都调用该方法
     * @param purchaseFormDto 保存入库单据和详情的类
     * @return 返回结果
     */
    @Override
    public int addPurchaseAndItem(PurchaseFormDto purchaseFormDto) {
        // 对于新增操作，不需要设置更新者，但是暂存原有数据应该要设置的吧

        // Service层来控制如何执行暂存操作
        // 首先判断是否已经存在，如果存在，先删除，然后进行插入操作
        // 入库单据id
        String purchaseId = purchaseFormDto.getPurchaseDto().getPurchaseId();
        PurchaseDto purchase = purchaseFormDto.getPurchaseDto();
        // 保存主表数据
        Purchase newPurchase = new Purchase();
        BeanUtil.copyProperties(purchase, newPurchase);
        if (null != purchase) {
            // 删除原数据
            // 删除入库单据信息表数据
            this.purchaseMapper.deleteById(purchaseId);
            // 删除入库单据详细信息表数据,根据入库单据id批量删除
            QueryWrapper<PurchaseItem> qw = new QueryWrapper<>();
            qw.eq(PurchaseItem.COL_PURCHASE_ID, purchaseId);
            this.purchaseItemMapper.delete(qw);
            // 设置要保存数据的更新者
            newPurchase.setUpdateBy(purchaseFormDto.getPurchaseDto().getSimpleUser().getUserName());
        }
        // 暂存时状态为未提交
        newPurchase.setStatus(Constants.STOCK_PURCHASE_STATUS_1);
        // 设置创建人
        newPurchase.setCreateBy(purchaseFormDto.getPurchaseDto().getSimpleUser().getUserName());
        // 设置创建时间
        newPurchase.setCreateTime(DateUtil.date());
        // 执行插入操作---保存入库单据主表数据
        int puchaseRes = this.purchaseMapper.insert(newPurchase);
        // 执行插入操作---保存入库单据详情数据
        for (PurchaseItemDto item : purchaseFormDto.getPurchaseItemDtos()) {
            PurchaseItem purchaseItem = new PurchaseItem();
            BeanUtil.copyProperties(item, purchaseItem);
            // 详情信息表中不保存创建和更新相关内容，由主表持有这些信息，因为每次都是重新insert
            purchaseItem.setPurchaseId(newPurchase.getPurchaseId());
            purchaseItem.setItemId(IdGeneratorSnowflake.generatorIdWithProfix(Constants.ID_PREFIX_CG));
            this.purchaseItemMapper.insert(purchaseItem);
        }
        return puchaseRes;
    }

    /**
     * 添加入库单据和详情信息并提交审核--新增和详情都调用该方法
     * @param purchaseFormDto 保存入库单据和详情的类
     * @return 返回结果
     */
    @Override
    public int addPurchaseAndItemToAudit(PurchaseFormDto purchaseFormDto) {
        // 对于新增操作，不需要设置更新者，但是暂存原有数据应该要设置的吧

        // Service层来控制如何执行暂存操作
        // 首先判断是否已经存在，如果存在，先删除，然后进行插入操作
        // 入库单据id
        String purchaseId = purchaseFormDto.getPurchaseDto().getPurchaseId();
        PurchaseDto purchase = purchaseFormDto.getPurchaseDto();
        // 保存主表数据
        Purchase newPurchase = new Purchase();
        BeanUtil.copyProperties(purchase, newPurchase);
        if (null != purchase) {
            // 删除原数据
            // 删除入库单据信息表数据
            this.purchaseMapper.deleteById(purchaseId);
            // 删除入库单据详细信息表数据,根据入库单据id批量删除
            QueryWrapper<PurchaseItem> qw = new QueryWrapper<>();
            qw.eq(PurchaseItem.COL_PURCHASE_ID, purchaseId);
            this.purchaseItemMapper.delete(qw);
            // 设置要保存数据的更新者
            newPurchase.setUpdateBy(purchaseFormDto.getPurchaseDto().getSimpleUser().getUserName());
        }
        // 提交审核时状态为待审核
        newPurchase.setStatus(Constants.STOCK_PURCHASE_STATUS_2);
        // 设置申请人
        // 设置申请人id
        newPurchase.setApplyUserName(purchaseFormDto.getPurchaseDto().getSimpleUser().getUserName());
        newPurchase.setApplyUserId(Long.valueOf(purchaseFormDto.getPurchaseDto().getSimpleUser().getUserId().toString()));
        // 设置创建人
        newPurchase.setCreateBy(purchaseFormDto.getPurchaseDto().getSimpleUser().getUserName());
        // 设置创建时间
        newPurchase.setCreateTime(DateUtil.date());
        // 执行插入操作---保存入库单据主表数据
        int puchaseRes = this.purchaseMapper.insert(newPurchase);
        // 执行插入操作---保存入库单据详情数据
        for (PurchaseItemDto item : purchaseFormDto.getPurchaseItemDtos()) {
            PurchaseItem purchaseItem = new PurchaseItem();
            BeanUtil.copyProperties(item, purchaseItem);
            // 详情信息表中不保存创建和更新相关内容，由主表持有这些信息，因为每次都是重新insert
            purchaseItem.setPurchaseId(newPurchase.getPurchaseId());
            purchaseItem.setItemId(IdGeneratorSnowflake.generatorIdWithProfix(Constants.ID_PREFIX_CG));
            this.purchaseItemMapper.insert(purchaseItem);
        }
        return puchaseRes;
    }

    /**
     * 入库【根据入库单据id】 操作stock_inventory_log表,并增加药品表stock_medicines表库存量
     * @param purchaseId 待入库的单据id
     * @return  返回结果
     */
    @Override
    public int doInventory(String purchaseId, SimpleUser user) {
        // 保存信息的同时，需要增加库存量
        Purchase purchase = this.purchaseMapper.selectById(purchaseId);
        // 查询该入库单据id对应的全部详情信息
        QueryWrapper<PurchaseItem> qw = new QueryWrapper<>();
        qw.eq(PurchaseItem.COL_PURCHASE_ID, purchaseId);
        List<PurchaseItem> purchaseItems = this.purchaseItemMapper.selectList(qw);
        try{
            for (PurchaseItem purchaseItem : purchaseItems) {
                InventoryLog inventoryLog = new InventoryLog();
                // 保存数据
                inventoryLog.setInventoryLogId(purchaseItem.getItemId());
                inventoryLog.setPurchaseId(purchaseItem.getPurchaseId());
                inventoryLog.setMedicinesId(purchaseItem.getMedicinesId());
                inventoryLog.setInventoryLogNum(purchaseItem.getPurchaseNumber());
                inventoryLog.setTradePrice(purchaseItem.getTradePrice());
                inventoryLog.setTradeTotalAmount(purchaseItem.getTradeTotalAmount());
                inventoryLog.setBatchNumber(purchaseItem.getBatchNumber());
                inventoryLog.setMedicinesName(purchaseItem.getMedicinesName());
                inventoryLog.setMedicinesType(purchaseItem.getMedicinesType());
                inventoryLog.setPrescriptionType(purchaseItem.getPrescriptionType());
                inventoryLog.setProducterId(purchaseItem.getProducterId());
                inventoryLog.setConversion(purchaseItem.getConversion());
                inventoryLog.setUnit(purchaseItem.getUnit());
                inventoryLog.setCreateTime(DateUtil.date());
                inventoryLog.setCreateBy(user.getUserName());
                // 执行插入操作  入库操作信息表
                this.inventoryLogMapper.insert(inventoryLog);
                // 更新药品库存
                Medicines medicines = this.medicinesMapper.selectById(purchaseItem.getMedicinesId());
                // 更新库存量
                medicines.setMedicinesStockNum(medicines.getMedicinesStockNum() + purchaseItem.getPurchaseNumber());
                // 设置更新人
                medicines.setUpdateBy(user.getUserName());
                this.medicinesMapper.updateById(medicines);
            }
            // 入库成功，修改入库单据状态为入库成功
            purchase.setStatus(Constants.STOCK_PURCHASE_STATUS_6);
            // 设置更新人
            purchase.setUpdateBy(user.getUserName());
            // 设置入库操作人和操作时间
            purchase.setStorageOptUser(user.getUserName());
            purchase.setStorageOptTime(DateUtil.date());
            this.purchaseMapper.updateById(purchase);
            return 1;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }
}
