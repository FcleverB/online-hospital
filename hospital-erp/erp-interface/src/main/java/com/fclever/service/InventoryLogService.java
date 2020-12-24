package com.fclever.service;

import com.fclever.dto.InventoryLogDto;
import com.fclever.vo.DataGridView;

/**
 * @author Fclever
 * @create 2020-12-24 02:29
 */
public interface InventoryLogService {

    /**
     * 分页查询批次库存及价格
     * @param inventoryLogDto 查询条件
     * @return  查询结果
     */
    DataGridView listInventoryLogPage(InventoryLogDto inventoryLogDto);
}
