package com.fclever.dto;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 入库单据和单据详情数据传输类
 * @author Fclever
 * @create 2020-12-20 12:08
 */
@ApiModel(value="com-fclever-dto-PurchaseFormDto")
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class PurchaseFormDto extends BaseDto{

    // 存放入库单据主表数据
    private PurchaseDto purchaseDto;

    // 存放入库单据详细信息
    private List<PurchaseItemDto> purchaseItemDtos;
}
