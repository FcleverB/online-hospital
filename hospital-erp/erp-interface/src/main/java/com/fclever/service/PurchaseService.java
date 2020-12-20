package com.fclever.service;

import com.fclever.domain.Purchase;
import com.fclever.domain.SimpleUser;
import com.fclever.dto.PurchaseDto;
import com.fclever.dto.PurchaseFormDto;
import com.fclever.vo.DataGridView;

/**
@author Fclever
@create 2020-12-14 15:21
*/
public interface PurchaseService {

    /**
     * 分页查询所有采购入库列表数据
     * @param purchaseDto   查询条件
     * @return  返回结果
     */
    DataGridView listPurchaseForPage(PurchaseDto purchaseDto);

    /**
     * 提交审核【根据入库单据id】
     *      状态为未提交和审核不通过可以进行该操作
     * @param purchaseId    需要提交的入库单据id
     * @param currentSimpleUser
     * @return  返回结果
     */
    int doAudit(String purchaseId, SimpleUser currentSimpleUser);

    /**
     * 作废【根据入库单据id】
     *      状态为未提交或者审核不通过才可以进行作废
     * @param purchaseId    需要作废的入库单据id
     * @return
     */
    int doInvalid(String purchaseId);

    /**
     * 审核通过【根据入库单据id】
     *      状态必须是待审核状态才能进行操作
     * @param purchaseId    需要审核通过的入库单据id
     * @return  返回结果
     */
    int auditPass(String purchaseId);

    /**
     * 审核不通过【根据入库单据id】
     *      状态必须为待审核状态
     * @param purchaseId    需要审核不通过的入库单据id
     * @param examine   审核不通过信息
     * @return  返回结果
     */
    int auditRefuse(String purchaseId, String examine);

    /**
     * 根据入库单据id查询对应的入库单据实体
     * @param purchaseId    待查询的入库单据id
     * @return  返回结果
     */
    Purchase getPurchaseById(String purchaseId);

    /**
     * 暂存入库单据和详情信息
     * @param purchaseFormDto 保存入库单据和详情的类
     * @return 返回结果
     */
    int addPurchaseAndItem(PurchaseFormDto purchaseFormDto);

    /**
     * 添加入库单据和详情信息并提交审核
     * @param purchaseFormDto 保存入库单据和详情的类
     * @return 返回结果
     */
    int addPurchaseAndItemToAudit(PurchaseFormDto purchaseFormDto);

    /**
     * 入库【根据入库单据id】
     * @param purchaseId 待入库的单据id
     * @return  返回结果
     */
    int doInventory(String purchaseId, SimpleUser user);
}
