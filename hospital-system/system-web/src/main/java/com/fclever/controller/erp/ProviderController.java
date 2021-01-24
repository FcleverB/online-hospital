package com.fclever.controller.erp;

import com.fclever.aspectj.annotation.Log;
import com.fclever.aspectj.enums.BusinessType;
import com.fclever.controller.BaseController;
import com.fclever.dto.ProviderDto;
import com.fclever.service.ProviderService;
import com.fclever.utils.ShiroSecurityUtils;
import com.fclever.vo.AjaxResult;
import com.fclever.vo.DataGridView;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author Fclever
 * @create 2020-12-13 13:59
 */
@RestController
@RequestMapping("erp/provider")
public class ProviderController extends BaseController {

    // @Autowired 是从当前SpringIOC容器中寻找，找不到，需要从Dubbo中查找
    @Reference // 引入dubbo中的包
    private ProviderService providerService;

    /**
     * 修改供应商信息
     * @param providerDto 修改的供应商信息
     * @return 返回结果
     */
    @PutMapping("updateProvider")
    @Log(title = "修改供应商信息",businessType = BusinessType.UPDATE)
    @HystrixCommand
    public AjaxResult updateProvider(@Validated ProviderDto providerDto){
        // 保存当前登录人做修改人
        providerDto.setSimpleUser(ShiroSecurityUtils.getCurrentSimpleUser());
        return AjaxResult.toAjax(this.providerService.updateProvider(providerDto));
    }

    /**
     * 分页查询供应商信息
     * @param providerDto 待修改的供应商信息
     * @return 返回结果
     */
    @GetMapping("listProviderForPage")
    @HystrixCommand
    public AjaxResult listProviderForPage(ProviderDto providerDto){
        DataGridView dataGridView = this.providerService.listProviderForPage(providerDto);
        return AjaxResult.success("分页查询供应商信息成功",dataGridView.getData(),dataGridView.getTotal());
    }

    /**
     * 根据id删除供应商（含批量）
     * @param providerIds 待删除的供应商id集合
     * @return 返回结果
     */
    @DeleteMapping("deleteProviderByIds/{providerIds}")
    @Log(title = "根据id删除供应商（含批量）",businessType = BusinessType.DELETE)
    @HystrixCommand
    public AjaxResult deleteProviderByIds(@PathVariable Long[] providerIds){
        return AjaxResult.toAjax(this.providerService.deleteProviderByIds(providerIds));
    }

    /**
     * 根据id查询对应的供应商信息
     * @param providerId 待查询的供应商id
     * @return 返回结果
     */
    @GetMapping("getProviderById/{providerId}")
    @HystrixCommand
    public AjaxResult getProviderById(@PathVariable Long providerId){
        return AjaxResult.success("查询指定数据成功", this.providerService.getProviderById(providerId));
    }

    /**
     * 查询所有可用的供应商信息
     * @return 返回结果
     */
    @GetMapping("selectAllProvider")
    @HystrixCommand
    public AjaxResult selectAllProvider(){
        return AjaxResult.success("查询可用的供应商信息成功",this.providerService.selectAllProvider());
    }

    /**
     * 添加供应商信息
     * @param providerDto 带添加的数据
     * @return 返回结果
     */
    @PostMapping("addProvider")
    @Log(title = "添加供应商信息",businessType = BusinessType.INSERT)
    @HystrixCommand
    public AjaxResult addProvider(@RequestBody @Validated ProviderDto providerDto){
        // 保存当前登录人为创建人
        providerDto.setSimpleUser(ShiroSecurityUtils.getCurrentSimpleUser());
        return AjaxResult.toAjax(this.providerService.addProvider(providerDto));
    }
}