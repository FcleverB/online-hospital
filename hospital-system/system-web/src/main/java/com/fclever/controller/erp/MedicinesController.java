package com.fclever.controller.erp;

import com.fclever.aspectj.annotation.Log;
import com.fclever.aspectj.enums.BusinessType;
import com.fclever.controller.BaseController;
import com.fclever.dto.MedicinesDto;
import com.fclever.service.MedicinesService;
import com.fclever.utils.ShiroSecurityUtils;
import com.fclever.vo.AjaxResult;
import com.fclever.vo.DataGridView;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 药品维护控制层
 * @author Fclever
 * @create 2020-12-12 22:36
 */
@RestController
@RequestMapping("erp/medicines")
public class MedicinesController extends BaseController {

    // @Autowired 是从当前SpringIOC容器中寻找，找不到，需要从Dubbo中查找
    @Reference // 引入dubbo中的包
    private MedicinesService medicinesService;

    /**
     * 修改药品信息信息
     * @param medicinesDto 修改的药品信息信息
     * @return 返回结果
     */
    @PutMapping("updateMedicines")
    @Log(title = "修改药品信息",businessType = BusinessType.UPDATE)
    @HystrixCommand
    public AjaxResult updateMedicines(@Validated MedicinesDto medicinesDto){
        // 保存当前登录人做修改人
        medicinesDto.setSimpleUser(ShiroSecurityUtils.getCurrentSimpleUser());
        return AjaxResult.toAjax(this.medicinesService.updateMedicines(medicinesDto));
    }

    /**
     * 分页查询药品信息信息
     * @param medicinesDto 待修改的药品信息信息
     * @return 返回结果
     */
    @GetMapping("listMedicinesForPage")
    @HystrixCommand
    public AjaxResult listMedicinesForPage(MedicinesDto medicinesDto){
        DataGridView dataGridView = this.medicinesService.listMedicinesForPage(medicinesDto);
        return AjaxResult.success("分页查询药品信息成功",dataGridView.getData(),dataGridView.getTotal());
    }

    /**
     * 根据id删除药品信息（含批量）
     * @param medicinesIds 待删除的药品信息id集合
     * @return 返回结果
     */
    @DeleteMapping("deleteMedicinesByIds/{medicinesIds}")
    @Log(title = "根据id删除药品信息（含批量）",businessType = BusinessType.DELETE)
    @HystrixCommand
    public AjaxResult deleteMedicinesByIds(@PathVariable Long[] medicinesIds){
        return AjaxResult.toAjax(this.medicinesService.deleteMedicinesByIds(medicinesIds));
    }

    /**
     * 根据id查询对应的药品信息信息
     * @param medicinesId 待查询的药品信息id
     * @return 返回结果
     */
    @GetMapping("getMedicinesById/{medicinesId}")
    @HystrixCommand
    public AjaxResult getMedicinesById(@PathVariable Long medicinesId){
        return AjaxResult.success("查询指定数据成功", this.medicinesService.getMedicinesById(medicinesId));
    }

    /**
     * 查询所有可用的药品信息信息
     * @return 返回结果
     */
    @GetMapping("selectAllMedicines")
    @HystrixCommand
    public AjaxResult selectAllMedicines(){
        return AjaxResult.success("查询可用的药品信息成功",this.medicinesService.selectAllMedicines());
    }

    /**
     * 添加药品信息信息
     * @param medicinesDto 带添加的数据
     * @return 返回结果
     */
    @PostMapping("addMedicines")
    @Log(title = "添加药品信息",businessType = BusinessType.INSERT)
    @HystrixCommand
    public AjaxResult addMedicines(@RequestBody @Validated MedicinesDto medicinesDto){
        // 保存当前登录人为创建人
        medicinesDto.setSimpleUser(ShiroSecurityUtils.getCurrentSimpleUser());
        return AjaxResult.toAjax(this.medicinesService.addMedicines(medicinesDto));
    }

    /**
     * 修改指定药品的库存量
     * @param medicinesId 待修改的药品主键id
     * @param medicinesStockNum 待修改的库存量
     * @return 返回结果
     */
    @PutMapping("updateMedicinesStock/{medicinesId}/{medicinesStockNum}")
    @Log(title = "修改药品信息",businessType = BusinessType.UPDATE)
    @HystrixCommand
    public AjaxResult updateMedicines(@PathVariable Long medicinesId, @PathVariable Long medicinesStockNum){
        // 保存当前登录人做修改人
        String userName = ShiroSecurityUtils.getCurrentSimpleUser().getUserName();
        return AjaxResult.toAjax(this.medicinesService.updateMedicinesStock(userName,medicinesId,medicinesStockNum));
    }
}
