package com.fclever.controller.erp;

import com.fclever.aspectj.annotation.Log;
import com.fclever.aspectj.enums.BusinessType;
import com.fclever.constants.Constants;
import com.fclever.controller.BaseController;
import com.fclever.domain.Purchase;
import com.fclever.domain.PurchaseItem;
import com.fclever.domain.SimpleUser;
import com.fclever.dto.PurchaseDto;
import com.fclever.dto.PurchaseFormDto;
import com.fclever.service.PurchaseItemService;
import com.fclever.service.PurchaseService;
import com.fclever.utils.IdGeneratorSnowflake;
import com.fclever.utils.ShiroSecurityUtils;
import com.fclever.vo.AjaxResult;
import com.fclever.vo.DataGridView;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import java.util.HashMap;
import java.util.List;

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
    @Log(title = "提交审核【根据入库单据id】",businessType = BusinessType.UPDATE)
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
    @Log(title = "作废【根据入库单据id】",businessType = BusinessType.UPDATE)
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
    @Log(title = "审核通过【根据入库单据id】",businessType = BusinessType.UPDATE)
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
    @Log(title = "审核不通过【根据入库单据id】",businessType = BusinessType.UPDATE)
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

    /**
     * 生成入库单据id
     * @return 雪花算法生成的入库单据id
     */
    @GetMapping("generatePurchaseId")
    public AjaxResult generatePurchaseId() {
        return AjaxResult.success(IdGeneratorSnowflake.generatorIdWithProfix(Constants.ID_PROFIX_CG));
    }

    /**
     * 暂存入库单据和详情信息--新增页面和查看详情页面都会调用该方法
     * @param purchaseFormDto 保存入库单据和详情的类
     * @return 返回结果
     */
    @PostMapping("addPurchase")
    @Log(title = "暂存入库单据和详情信息",businessType = BusinessType.INSERT)
    public AjaxResult addPurchase(@RequestBody PurchaseFormDto purchaseFormDto) {
        // Controller来判断能否进行该操作，Service层来控制如何进行该操作
        String purchaseId = purchaseFormDto.getPurchaseDto().getPurchaseId();
        // 由于新增页面和查看详情页面都调用该方法，需要进行判断
        /**
         * 入库单据id，已经存在于数据库
         *                  不是未提交或者审核不通过状态，不能进行暂存操作
         *                  是未提交或者审核不通过，可以进行暂存操作
         *          ，不存在与数据库
         *                  可以进行暂存操作，不判断状态
         */
        if (isPurchaseExist(purchaseId)){
            purchaseFormDto.getPurchaseDto().setSimpleUser(ShiroSecurityUtils.getCurrentSimpleUser());
            return AjaxResult.toAjax(this.purchaseService.addPurchaseAndItem(purchaseFormDto));
        }
        return AjaxResult.fail("当前单据状态不是【未提交】或【审核不通过】状态，不能进行暂存操作！");
    }

    /**
     * 添加入库单据和详情信息并提交审核--新增页面和查看详情页面都会调用该方法
     * @param purchaseFormDto 保存listPurchasePendingForPage入库单据和详情的类
     * @return 返回结果
     */
    @PostMapping("addPurchaseToAudit")
    @Log(title = "添加入库单据和详情信息并提交审核",businessType = BusinessType.INSERT)
    public AjaxResult addPurchaseToAudit(@RequestBody PurchaseFormDto purchaseFormDto) {
        // Controller来判断能否进行该操作，Service层来控制如何进行该操作
        String purchaseId = purchaseFormDto.getPurchaseDto().getPurchaseId();
        // 由于新增页面和查看详情页面都调用该方法，需要进行判断
        /**
         * 入库单据id，已经存在于数据库
         *                  不是未提交或者审核不通过状态，不能进行暂存操作
         *                  是未提交或者审核不通过，可以进行暂存操作
         *          ，不存在与数据库
         *                  可以进行暂存操作，不判断状态
         */
        if (isPurchaseExist(purchaseId)){
            purchaseFormDto.getPurchaseDto().setSimpleUser(ShiroSecurityUtils.getCurrentSimpleUser());
            return AjaxResult.toAjax(this.purchaseService.addPurchaseAndItemToAudit(purchaseFormDto));
        }
        return AjaxResult.fail("当前单据状态不是【未提交】或【审核不通过】状态，不能进行提交审核操作！");
    }

    /**
     * 判断用户是否可以进行暂存操作，两步判断逻辑不可以调换顺序
     * @return 判断结果
     */
    private boolean isPurchaseExist(String purchaseId) {
        Purchase purchase = this.purchaseService.getPurchaseById(purchaseId);
        //判断ID在数据库里面是否存在
        if (null == purchase) {
            // 不存在于数据库，可以进行暂存操作
            return true;
        }
        if (purchase.getStatus().equals(Constants.STOCK_PURCHASE_STATUS_1)
                || purchase.getStatus().equals(Constants.STOCK_PURCHASE_STATUS_4)) {
            // 存在于数据库，状态符合要求，可以进行暂存操作
            return true;
        }
        return false;
    }

    /**
     * 根据入库单据id查询入库单据信息和详情信息
     * @param purchaseId 待查询的入库单据id
     * @return  查询结果
     */
    @GetMapping("queryPurchaseAndItemByPurchaseId/{purchaseId}")
    public AjaxResult queryPurchaseAndItemByPurchaseId(@PathVariable String purchaseId){
        // 查询入库单据信息
        Purchase purchase = this.purchaseService.getPurchaseById(purchaseId);
        // 查询对应的详情信息
        List<PurchaseItem> purchaseItems = this.purchaseItemService.getPurchaseItemById(purchaseId);
        // 封装结果保存
        HashMap<String, Object> res = new HashMap<>();
        res.put("purchase", purchase);
        res.put("purchaseItems", purchaseItems);
        return AjaxResult.success(res);
    }

    /**
     * 入库【根据入库单据id】
     * @param purchaseId 待入库的单据id
     * @return  返回结果
     */
    @PutMapping("doInventory/{purchaseId}")
    @Log(title = "入库【根据入库单据id】",businessType = BusinessType.UPDATE)
    public AjaxResult doInventory(@PathVariable String purchaseId){
        // 根据审核单据id查询对应的入库单据实体，根据状态来判断是否可以执行该操作
        Purchase purchase = this.purchaseService.getPurchaseById(purchaseId);
        if (purchase.getStatus().equals(Constants.STOCK_PURCHASE_STATUS_3)){
            // 获取入库操作人
            SimpleUser user = ShiroSecurityUtils.getCurrentSimpleUser();
            return AjaxResult.toAjax(this.purchaseService.doInventory(purchaseId,user));
        }
        return AjaxResult.fail("当前单据状态不是【审核通过】状态，不能进行入库！");
    }
}
