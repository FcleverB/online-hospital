package com.fclever.controller.diagnose;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.fclever.aspectj.annotation.Log;
import com.fclever.aspectj.enums.BusinessType;
import com.fclever.constants.Constants;
import com.fclever.controller.BaseController;
import com.fclever.domain.CareHistory;
import com.fclever.domain.CareOrder;
import com.fclever.domain.CareOrderItem;
import com.fclever.domain.CheckResult;
import com.fclever.dto.CheckResultDto;
import com.fclever.service.CareHistoryService;
import com.fclever.service.CareOrderItemService;
import com.fclever.service.CareOrderService;
import com.fclever.service.CheckResultService;
import com.fclever.utils.IdGeneratorSnowflake;
import com.fclever.utils.ShiroSecurityUtils;
import com.fclever.vo.AjaxResult;
import com.fclever.vo.DataGridView;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import io.swagger.models.auth.In;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.*;

/**
 * 检查管理控制层
 * @author Fclever
 * @create 2021-02-15 14:08
 */
@RestController
@RequestMapping("doctor/check")
public class CheckResultController extends BaseController {

    @Reference
    private CareHistoryService careHistoryService;

    @Reference
    private CareOrderService careOrderService;

    @Reference
    private CareOrderItemService careOrderItemService;

    @Reference
    private CheckResultService checkResultService;

    /**
     * 根据挂号单号和检查项目Id集合查询要检查的项目（已支付的检查项）
     *      这里传递的是JSON对象，需要使用POST请求方式
     * @param checkResultDto    查询条件    挂号单据Id | 检查项目Id集合
     * @return  返回结果
     */
    @PostMapping("queryNeedCheckItem")
    @HystrixCommand
    public AjaxResult queryNeedCheckItem(@RequestBody CheckResultDto checkResultDto) {
        // 声明返回对象，返回结果是List《检查项目对象》
        ArrayList<CareOrderItem> result = new ArrayList<>();
        if (StringUtils.isNotBlank(checkResultDto.getRegistrationId())) {
            // 查询条件代入挂号单据Id
            CareHistory careHistory = this.careHistoryService.queryCareHistoryByRegistrationId(checkResultDto.getRegistrationId());
            if (null == careHistory) {
                // 如果挂号单据id不存在病历信息
                return AjaxResult.success(result);
            }
            // 根据病历id查询所有处方信息
            List<CareOrder> careOrders = this.careOrderService.queryCareOrdersByChId(careHistory.getChId());
            for (CareOrder careOrder : careOrders) {
                if (careOrder.getCoType().equals(Constants.ITEM_TYPE_CHECK)) { // 只查询检查处方
                    List<CareOrderItem> careOrderItemList = this.careOrderItemService.queryCareOrderItemsChargedByCoId(careOrder.getCoId());
                    for (CareOrderItem careOrderItem : careOrderItemList) {
                        if (checkResultDto.getCheckItemIds().contains(Integer.valueOf(careOrderItem.getItemRefId()))) {
                            result.add(careOrderItem);
                        }
                    }
                }
            }
            return AjaxResult.success(result);
        } else {
            // 查询已支付的检查的项目
            List<CareOrderItem> careOrderItemList = this.careOrderItemService.queryCareOrderItemsChargedAndChecked(Constants.ITEM_TYPE_CHECK, Constants.ORDER_DETAILS_STATUS_1);
            for (CareOrderItem careOrderItem : careOrderItemList) {
                if (checkResultDto.getCheckItemIds().contains(Integer.valueOf(careOrderItem.getItemRefId()))) {
                    result.add(careOrderItem);
                }
            }
            return AjaxResult.success(result);
        }
    }

    /**
     * 根据详情Id查询对应病历|处方|处方详情
     * @param itemId    处方详情Id
     * @return  查询结果
     */
    @GetMapping("queryCheckItemByItemId/{itemId}")
    @HystrixCommand
    public AjaxResult queryCheckItemByItemId(@PathVariable String itemId) {
        // 根据详情Id获取详情信息
        CareOrderItem careOrderItem = this.careOrderItemService.queryCareOrderItemByItemId(itemId);
        if(careOrderItem==null){
            return AjaxResult.fail("【"+itemId+"】的检查单号的详情数据不存在，请核对后再查询");
        }
        if(!careOrderItem.getStatus().equals(Constants.ORDER_DETAILS_STATUS_1)){
            return AjaxResult.fail("【"+itemId+"】的检查单号没有支付，请支付后再查询");
        }
        if(!careOrderItem.getItemType().equals(Constants.ITEM_TYPE_CHECK)){
            return AjaxResult.fail("【"+itemId+"】的单号不是检查项目，请核对再查询");
        }
        // 根据处方id查询处方信息
        CareOrder careOrder = this.careOrderService.queryCareOrderByCoId(careOrderItem.getCoId());
        // 根据病历Id查询病历信息
        CareHistory careHistory = this.careHistoryService.queryCareHistoryByChId(careOrder.getChId());
        // 封装返回数据
        Map<String, Object> map = new HashMap<>();
        map.put("careHistory", careHistory);
        map.put("careOrder", careOrder);
        map.put("careOrderItem", careOrderItem);
        return AjaxResult.success(map);
    }

    /**
     * 开始检查（针对已支付的检查项）
     * @param itemId    处方详情Id
     * @return  返回结果
     */
    @PostMapping("startCheck/{itemId}")
    @HystrixCommand
    @Log(title = "开始检查", businessType = BusinessType.INSERT)
    public AjaxResult startCheck(@PathVariable String itemId) {
        // 根据详情Id获取详情信息
        CareOrderItem careOrderItem = this.careOrderItemService.queryCareOrderItemByItemId(itemId);
        if(careOrderItem==null){
            return AjaxResult.fail("【"+itemId+"】的检查单号的详情数据不存在，请核对后再查询");
        }
        if(!careOrderItem.getStatus().equals(Constants.ORDER_DETAILS_STATUS_1)){
            return AjaxResult.fail("【"+itemId+"】的检查单号没有支付，请支付后再查询");
        }
        if(!careOrderItem.getItemType().equals(Constants.ITEM_TYPE_CHECK)){
            return AjaxResult.fail("【"+itemId+"】的单号不是检查项目，请核对再查询");
        }
        // 根据处方id查询处方信息
        CareOrder careOrder = this.careOrderService.queryCareOrderByCoId(careOrderItem.getCoId());
        // 根据病历Id查询病历信息
        CareHistory careHistory = this.careHistoryService.queryCareHistoryByChId(careOrder.getChId());
        // 封装保存的数据对象
        CheckResult checkResult = new CheckResult();
        checkResult.setItemId(itemId);
        checkResult.setCheckItemId(Integer.parseInt(careOrderItem.getItemRefId()));
        checkResult.setCheckItemName(careOrderItem.getItemName());
        checkResult.setPrice(careOrderItem.getPrice());
        checkResult.setPatientId(careHistory.getPatientId());
        checkResult.setPatientName(careHistory.getPatientName());
        checkResult.setRegistrationId(careHistory.getRegistrationId());
        checkResult.setCreateBy(ShiroSecurityUtils.getCurrentUserName());
        checkResult.setCreateTime(DateUtil.date());
        checkResult.setResultStatus(Constants.CHECK_RESULT_STATUS_0); // 检查中
        this.checkResultService.startCheck(checkResult);
        return AjaxResult.success();
    }

    /**
     * 查询所有检查中的项目
     * @param checkResultDto    查询条件
     * @return  返回结果
     */
    @PostMapping("queryAllCheckingResultForPage")
    @HystrixCommand
    public AjaxResult queryAllCheckingResultForPage(@RequestBody CheckResultDto checkResultDto) {
        DataGridView dataGridView = this.checkResultService.queryAllCheckingResultForPage(checkResultDto);
        return AjaxResult.success("分页查询检查中项目成功", dataGridView.getData(),dataGridView.getTotal());
    }
}
