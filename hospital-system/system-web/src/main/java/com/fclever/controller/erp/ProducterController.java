package com.fclever.controller.erp;

import com.fclever.aspectj.annotation.Log;
import com.fclever.aspectj.enums.BusinessType;
import com.fclever.controller.BaseController;
import com.fclever.dto.ProducterDto;
import com.fclever.service.ProducterService;
import com.fclever.utils.ShiroSecurityUtils;
import com.fclever.vo.AjaxResult;
import com.fclever.vo.DataGridView;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 生产厂家控制层
 * @author Fclever
 * @create 2020-12-08 09:14
 */
@RestController
@RequestMapping("erp/producter")
public class ProducterController extends BaseController {

    // @Autowired 是从当前SpringIOC容器中寻找，找不到，需要从Dubbo中查找
    @Reference // 引入dubbo中的包
    private ProducterService producterService;

    /**
     * 修改生产厂家信息
     * @param producterDto 修改的生产厂家信息
     * @return 返回结果
     */
    @PutMapping("updateProducter")
    @Log(title = "修改生产厂家信息",businessType = BusinessType.UPDATE)
    @HystrixCommand
    public AjaxResult updateProducter(@Validated ProducterDto producterDto){
        // 保存当前登录人做修改人
        producterDto.setSimpleUser(ShiroSecurityUtils.getCurrentSimpleUser());
        return AjaxResult.toAjax(this.producterService.updateProducter(producterDto));
    }

    /**
     * 分页查询生产厂家信息
     * @param producterDto 待修改的生产厂家信息
     * @return 返回结果
     */
    @GetMapping("listProducterForPage")
    @HystrixCommand
    public AjaxResult listProducterForPage(ProducterDto producterDto){
        DataGridView dataGridView = this.producterService.listProducterForPage(producterDto);
        return AjaxResult.success("分页查询生产厂家信息成功",dataGridView.getData(),dataGridView.getTotal());
    }

    /**
     * 根据id删除生产厂家（含批量）
     * @param producterIds 待删除的生产厂家id集合
     * @return 返回结果
     */
    @DeleteMapping("deleteProducterByIds/{producterIds}")
    @Log(title = "根据id删除生产厂家（含批量）",businessType = BusinessType.DELETE)
    @HystrixCommand
    public AjaxResult deleteProducterByIds(@PathVariable Long[] producterIds){
        return AjaxResult.toAjax(this.producterService.deleteProducterByIds(producterIds));
    }

    /**
     * 根据id查询对应的生产厂家信息
     * @param producterId 待查询的生产厂家id
     * @return 返回结果
     */
    @GetMapping("getProducterById/{producterId}")
    @HystrixCommand
    public AjaxResult getProducterById(@PathVariable Long producterId){
        return AjaxResult.success("查询指定数据成功", this.producterService.getProducterById(producterId));
    }

    /**
     * 查询所有可用的生产厂家信息
     * @return 返回结果
     */
    @GetMapping("selectAllProducter")
    @HystrixCommand
    public AjaxResult selectAllProducter(){
        return AjaxResult.success("查询可用的生产厂家信息成功",this.producterService.selectAllProducter());
    }

    /**
     * 添加生产厂家信息
     * @param producterDto 带添加的数据
     * @return 返回结果
     */
    @PostMapping("addProducter")
    @Log(title = "添加生产厂家信息",businessType = BusinessType.INSERT)
    @HystrixCommand
    public AjaxResult addProducter(@RequestBody @Validated ProducterDto producterDto){
        // 保存当前登录人为创建人
        producterDto.setSimpleUser(ShiroSecurityUtils.getCurrentSimpleUser());
        return AjaxResult.toAjax(this.producterService.addProducter(producterDto));
    }
}
