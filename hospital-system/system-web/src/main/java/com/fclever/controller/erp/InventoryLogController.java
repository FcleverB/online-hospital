package com.fclever.controller.erp;

import com.fclever.controller.BaseController;
import com.fclever.dto.InventoryLogDto;
import com.fclever.service.InventoryLogService;
import com.fclever.vo.AjaxResult;
import com.fclever.vo.DataGridView;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 库存相关控制层
 * @author Fclever
 * @create 2020-12-24 02:44
 */
@RestController
@RequestMapping("erp/inventoryLog")
public class InventoryLogController extends BaseController {

    // Dubbo引用
    @Reference
    private InventoryLogService inventoryLogService;

    @GetMapping("listInventoryLogForPage")
    public AjaxResult listInventoryLogPage(InventoryLogDto inventoryLogDto) {
        DataGridView dataGridView = this.inventoryLogService.listInventoryLogPage(inventoryLogDto);
        return AjaxResult.success("分页查询数据成功", dataGridView.getData(), dataGridView.getTotal());
    }
}
