package com.fclever.controller.erp;

import com.fclever.constants.Constants;
import com.fclever.controller.BaseController;
import com.fclever.domain.Purchase;
import com.fclever.domain.SimpleUser;
import com.fclever.dto.PurchaseDto;
import com.fclever.service.PurchaseItemService;
import com.fclever.service.PurchaseService;
import com.fclever.utils.ShiroSecurityUtils;
import com.fclever.vo.AjaxResult;
import com.fclever.vo.DataGridView;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.*;

/**
 * 入库单据控制层
 * @author Fclever
 * @create 2020-12-14 15:46
 */
@RestController
@RequestMapping("erp/purchase")
public class PurchaseController extends BaseController {

    @Reference
    private PurchaseService purchaseService;

    @Reference
    private PurchaseItemService purchaseItemService;

    /**
     * 分页查询所有采购入库列表数据
     * @param purchaseDto   查询条件
     * @return  返回结果
     */
    @GetMapping("listPurchaseForPage")
    @HystrixCommand
    public AjaxResult listPurchaseForPage(PurchaseDto purchaseDto){
        DataGridView dataGridView = this.purchaseService.listPurchaseForPage(purchaseDto);
        return AjaxResult.success("分页数据查询成功",dataGridView.getData(),dataGridView.getTotal());
    }

    /**
     * 提交审核【根据入库单据id】
     *      状态为未提交和审核不通过可以进行该操作
     * @param purchaseId    需要提交的入库单据id
     * @return  返回结果
     */
    @PutMapping("doAudit/{purchaseId}")
    @HystrixCommand
    public AjaxResult doAudit(@PathVariable String purchaseId){
        // 根据审核单据id查询对应的入库单据实体，根据状态来判断是否可以执行该操作
        Purchase purchase = this.purchaseService.getPurchaseById(purchaseId);
        if (purchase.getStatus().equals(Constants.STOCK_PURCHASE_STATUS_1) || purchase.getStatus().equals(Constants.STOCK_PURCHASE_STATUS_4)){
            // 符合条件，保存申请人信息
            SimpleUser currentSimpleUser = ShiroSecurityUtils.getCurrentSimpleUser();
            return AjaxResult.toAjax(this.purchaseService.doAudit(purchaseId, currentSimpleUser));
        }
        return AjaxResult.fail("当前单据状态不是【未提交】或【审核不通过】状态，不能提交审核！");
    }

    /**
     * 作废【根据入库单据id】
     *      状态为未提交或者审核不通过才可以进行作废
     * @param purchaseId    需要作废的入库单据id
     * @return
     */
    @PutMapping("doInvalid/{purchaseId}")
    @HystrixCommand
    public AjaxResult doInvalid(@PathVariable String purchaseId){
        // 根据审核单据id查询对应的入库单据实体，根据状态来判断是否可以执行该操作
        Purchase purchase = this.purchaseService.getPurchaseById(purchaseId);
        if (purchase.getStatus().equals(Constants.STOCK_PURCHASE_STATUS_1) || purchase.getStatus().equals(Constants.STOCK_PURCHASE_STATUS_4)){
            return AjaxResult.toAjax(this.purchaseService.doInvalid(purchaseId));
        }
        return AjaxResult.fail("当前单据状态不是【未提交】或【审核不通过】状态，不能进行作废！");
    }

    /**
     * 分页查询所有待审核的采购入库列表数据
     * @param purchaseDto   查询条件
     * @return  返回结果
     */
    @GetMapping("listPurchasePendingForPage")
    @HystrixCommand
    public AjaxResult listPurchasePendingForPage(PurchaseDto purchaseDto){
        // 设置状态为待审核
        purchaseDto.setStatus(Constants.STOCK_PURCHASE_STATUS_2);
        DataGridView dataGridView = this.purchaseService.listPurchaseForPage(purchaseDto);
        return AjaxResult.success("分页数据查询成功",dataGridView.getData(),dataGridView.getTotal());
    }

    /**
     * 审核通过【根据入库单据id】
     *      状态必须是待审核状态才能进行操作
     * @param purchaseId    需要审核通过的入库单据id
     * @return  返回结果
     */
    @PutMapping("auditPass/{purchaseId}")
    @HystrixCommand
    public AjaxResult auditPass(@PathVariable String purchaseId){
        // 根据审核单据id查询对应的入库单据实体，根据状态来判断是否可以执行该操作
        Purchase purchase = this.purchaseService.getPurchaseById(purchaseId);
        if (purchase.getStatus().equals(Constants.STOCK_PURCHASE_STATUS_2)){
            return AjaxResult.toAjax(this.purchaseService.auditPass(purchaseId));
        }
        return AjaxResult.fail("当前单据状态不是【待审核】状态，不能进行审核通过！");
    }

    /**
     * 审核不通过【根据入库单据id】
     *      状态必须为待审核状态
     * @param purchaseId    需要审核不通过的入库单据id
     * @param auditMsg   审核不通过信息
     * @return  返回结果
     */
    @PutMapping("auditRefuse/{purchaseId}/{auditMsg}")
    @HystrixCommand
    public AjaxResult auditRefuse(@PathVariable String purchaseId, @PathVariable String auditMsg){
        // 根据审核单据id查询对应的入库单据实体，根据状态来判断是否可以执行该操作
        Purchase purchase = this.purchaseService.getPurchaseById(purchaseId);
        if (purchase.getStatus().equals(Constants.STOCK_PURCHASE_STATUS_2)){
            return AjaxResult.toAjax(this.purchaseService.auditRefuse(purchaseId,auditMsg));
        }
        return AjaxResult.fail("当前单据状态不是【待审核】状态，不能进行审核不通过！");
    }

    /**
     * 根据入库单据id查询入库单据详情信息
     * @param purchaseId    需要查询的入库单据id
     * @return  返回结果
     */
    @GetMapping("getPurchaseItemById/{purchaseId}")
    @HystrixCommand
    public AjaxResult getPurchaseItemById(@PathVariable String purchaseId){
        return AjaxResult.success(this.purchaseItemService.getPurchaseItemById(purchaseId));
    }
}
